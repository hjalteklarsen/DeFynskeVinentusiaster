document.addEventListener("DOMContentLoaded", async () => {
    const memberSelect = document.getElementById("memberSelect");
    const modeSelect = document.getElementById("modeSelect");
    const promptInput = document.getElementById("promptInput");
    const results = document.getElementById("results");
    const recommendBtn = document.getElementById("recommendBtn");

    // === Load members for dropdown ===
    async function loadMembersDropdown() {
        try {
            const res = await fetch("/internal/members");
            const members = await res.json();
            memberSelect.innerHTML = '<option value="">-- Vælg medlem --</option>';
            members.forEach(m => {
                const opt = document.createElement("option");
                opt.value = m.id;
                opt.textContent = m.displayName;
                memberSelect.appendChild(opt);
            });
        } catch (err) {
            console.error("Kunne ikke hente medlemmer:", err);
        }
    }

    await loadMembersDropdown();

    // === Handle recommendation request ===
    recommendBtn.addEventListener("click", async () => {
        const memberId = memberSelect.value;
        const mode = modeSelect.value;
        const prompt = promptInput.value.trim();

        if (!memberId) {
            alert("Vælg venligst et medlem først.");
            return;
        }

        results.innerHTML = '<p class="loading">Indlæser anbefalinger...</p>';

        try {
            const res = await fetch("/internal/recommendations", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ memberId, mode, prompt })
            });

            const data = await res.json();
            results.innerHTML = "";

            if (data.message) {
                results.innerHTML = `<p>${data.message}</p>`;
                return;
            }

            if (!data.recommendations) {
                results.innerHTML = `<pre>${JSON.stringify(data, null, 2)}</pre>`;
                return;
            }

            data.recommendations.forEach(r => {
                const card = document.createElement("div");
                card.className = "recommendation-card ftco-animate";
                card.innerHTML = `
          <h5>${r.name || "(ukendt vin)"}</h5>
          ${r.region ? `<p><strong>Region:</strong> ${r.region}</p>` : ""}
          ${r.grape ? `<p><strong>Druer:</strong> ${r.grape}</p>` : ""}
          <p class="text-muted" style="white-space: pre-line;">${r.reason}</p>
        `;
                results.appendChild(card);
            });
        } catch (err) {
            results.innerHTML = "<p>Der opstod en fejl ved hentning af anbefalinger.</p>";
            console.error(err);
        }
    });
});
