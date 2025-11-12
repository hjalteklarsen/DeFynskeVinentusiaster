document.addEventListener("DOMContentLoaded", async () => {
    const head = document.getElementById("ratingsHead");
    const body = document.getElementById("ratingsBody");
    const addWineBtn = document.getElementById("addWineBtn");

    // === Load members for "Bragt af" dropdown ===
    async function loadMembersDropdown() {
        try {
            const res = await fetch("/internal/members");
            const members = await res.json();
            const select = document.getElementById("broughtBy");
            select.innerHTML = '<option value="">-- Vælg medlem --</option>';
            members.forEach(m => {
                const opt = document.createElement("option");
                opt.value = m.id;
                opt.textContent = m.displayName;
                select.appendChild(opt);
            });
        } catch (err) {
            console.error("Kunne ikke hente medlemmer:", err);
        }
    }
    await loadMembersDropdown();

    addWineBtn.addEventListener("click", () => {
        $("#addWineModal").modal("show");
    });

    document.getElementById("addWineForm").addEventListener("submit", async (e) => {
        e.preventDefault();

        const meetingId = new URLSearchParams(window.location.search).get("id");
        const payload = {
            wineName: document.getElementById("wineName").value,
            country: document.getElementById("country").value,
            category: document.getElementById("category").value,
            addedById: parseInt(document.getElementById("broughtBy").value),
            sequenceNo: parseInt(document.getElementById("sequenceNo").value),
            vivinoScore: parseFloat(document.getElementById("vivinoRating").value),
            vivinoUrl: document.getElementById("vivinoUrl").value,
        };

        try {
            const res = await fetch(`/internal/meeting/${meetingId}/wines`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(payload)
            });

            if (res.ok) {
                $("#addWineModal").modal("hide");
                alert("Vinen blev tilføjet!");
                location.reload();
            } else {
                alert("Fejl ved tilføjelse af vin.");
            }
        } catch (err) {
            console.error("Fejl:", err);
        }
    });

    // === Load meeting details ===
    const meetingId = new URLSearchParams(window.location.search).get("id");
    const res = await fetch(`/internal/meeting/${meetingId}`);
    if (!res.ok) {
        console.error("Kunne ikke hente mødet:", res.status);
        return;
    }
    const meeting = await res.json();

    // === Populate meeting header ===
    document.getElementById("meetingTitle").textContent = meeting.description;
    document.getElementById("meetingMeta").textContent =
        `Dato: ${new Date(meeting.date).toLocaleDateString("da-DK")} | Vært: ${meeting.hostDisplayName}`;

    const memberNames = meeting.members ?? Object.keys(meeting.wines[0]?.ratings ?? {});
    head.innerHTML = `
        <tr>
            <th>#</th>
            <th>Vin</th>
            <th>Bragt af</th>
            ${memberNames.map(m => `<th>${m}</th>`).join("")}
            <th>DFV</th>
            <th>Vivino</th>
        </tr>
    `;

    // === Render wines ===
    function renderTable() {
        body.innerHTML = "";
        meeting.wines.forEach((wine, i) => {
            const ratingsHTML = meeting.members.map(m => {
                const val = wine.ratings?.[m] ?? "–";
                return `<td class="editable" data-wine="${wine.id}" data-member="${m}">
                    ${val !== "–" && !isNaN(val) ? Number(val).toFixed(1) : "–"}
                </td>`;
            }).join("");

            const row = document.createElement("tr");
            row.classList.add("wine-row");
            row.dataset.wineId = wine.id;

            // DFV always visible
            const dfvCell = `<td>${wine.dfv.toFixed(1)}</td>`;

            // Vivino hidden until revealed per wine
            const vivinoCell = `
                <td>
                    <span id="vivino-val-${wine.id}" class="d-none">
                        ${wine.vivinoScore ? wine.vivinoScore.toFixed(1) : "–"}
                    </span>
                    <button class="btn btn-sm btn-outline-dark reveal-vivino" data-wine-id="${wine.id}">
                        Afslør Vivino-rating
                    </button>
                </td>
            `;

            row.innerHTML = `
                <td>${i + 1}</td>
                <td>${wine.wineName}</td>
                <td>${wine.broughtByName ?? "-"}</td>
                ${ratingsHTML}
                ${dfvCell}
                ${vivinoCell}
            `;
            body.appendChild(row);
        });
    }

    renderTable();

    // === Inline editing ===
    body.addEventListener("click", e => {
        const cell = e.target.closest(".editable");
        if (!cell) return;

        const oldValue = cell.textContent.trim();
        const input = document.createElement("input");
        input.type = "number";
        input.step = "0.1";
        input.min = "0";
        input.max = "10";
        input.value = oldValue === "–" ? "" : oldValue;
        input.className = "form-control form-control-sm text-center";
        input.style.width = "60px";

        cell.innerHTML = "";
        cell.appendChild(input);
        input.focus();

        input.addEventListener("blur", () => saveRating(cell, input.value));
        input.addEventListener("keydown", ev => {
            if (ev.key === "Enter") input.blur();
        });
    });

    async function saveRating(cell, value) {
        const wineId = parseInt(cell.dataset.wine);
        const member = cell.dataset.member;
        const numVal = parseFloat(value);

        const wine = meeting.wines.find(w => w.id === wineId);
        if (!wine) return;

        wine.ratings[member] = isNaN(numVal) ? 0 : numVal;

        // Update DFV average
        const valid = Object.values(wine.ratings).filter(v => v > 0);
        wine.dfv = valid.length ? valid.reduce((a, b) => a + b, 0) / valid.length : 0;

        await fetch(`/internal/meeting/${meeting.id}/rating`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                wineId: wineId,
                meetingId: meeting.id,
                memberName: member,
                dfvScore: numVal
            })
        });

        renderTable();
    }

    // === Per-wine Vivino reveal ===
    body.addEventListener("click", e => {
        const btn = e.target.closest(".reveal-vivino");
        if (!btn) return;

        const id = btn.getAttribute("data-wine-id");
        const valEl = document.getElementById(`vivino-val-${id}`);
        if (valEl) valEl.classList.remove("d-none");

        btn.remove(); // remove the reveal button
    });
});
