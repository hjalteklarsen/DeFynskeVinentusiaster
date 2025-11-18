document.addEventListener("DOMContentLoaded", () => {
    setTimeout(initRatings, 200); // wait for components to load
});

const API_BASE_URL = location.hostname === "localhost"
    ? "http://localhost:8080"
    : "http://157.180.23.204:8080";

async function initRatings() {
    const container = document.getElementById("ratings-container");
    if (!container) return console.error("❌ No #ratings-container found");

    let ratings = [];
    const sortState = { dfv: true, vivino: true, date: true };

    // --- fetch & render ---
    await loadRatings();

    async function loadRatings() {
        try {
            const res = await fetch(`${API_BASE_URL}/api/ratings`);
            if (!res.ok) throw new Error("Failed to fetch ratings");
            ratings = await res.json();
            console.log("✅ Ratings fetched:", ratings);
            renderRatings(ratings);
        } catch (err) {
            console.error(err);
            container.innerHTML =
                `<p class="text-danger text-center mt-5">Kunne ikke hente bedømmelser 😢</p>`;
        }
    }

    function renderRatings(data) {
        container.innerHTML = "";
        if (!data || data.length === 0) {
            container.innerHTML = "<p class='text-center'>Ingen bedømmelser fundet.</p>";
            return;
        }

        data.forEach(r => {
            const card = document.createElement("div");
            const wine = r.wine || {};
            card.className = "col-md-3 d-flex mb-4";
            card.innerHTML = `
        <div class="product ftco-animate">
          <div class="img" style="background-image:url('${r.imageUrl || "images/default-wine.jpg"}')"></div>
          <div class="text text-center p-3">
            <h3>${r.wineName}</h3>
            <p class="category text-muted mb-1">${r.country}</p>
            <p><strong>DFV:</strong> ${r.dfvAvg.toFixed(1)} | 
               <strong>Vivino:</strong> ${r.vivinoScore ? r.vivinoScore.toFixed(1) : "-"}</p>

            <p class="small text-muted mb-0">
              Møde-dato: ${r.meetingDate ? new Date(r.meetingDate).toLocaleDateString("da-DK") : "Ukendt dato"}<br>
              Vært: ${r.hostName || "Ukendt"}
            </p>
          </div>
        </div>`;
            card.addEventListener("click", () => showRatingsModal(r));
            container.appendChild(card);
        });

        console.log(`🎨 Rendered ${data.length} cards`);
    }

    function showRatingsModal(wine) {
        const modalBody = document.getElementById("ratingsModalBody");
        modalBody.innerHTML = `
    <h4>${wine.wineName}</h4>
    <p><strong>Gennemsnit DFV:</strong> ${wine.dfvAvg.toFixed(1)}</p>
    <table class="table table-sm">
      <thead><tr><th>Medlem</th><th>Rating</th></tr></thead>
      <tbody>
        ${wine.ratings && wine.ratings.length > 0
            ? wine.ratings.map(r =>
                `<tr><td>${r.member}</td><td>${r.score.toFixed(1)}</td></tr>`
            ).join("")
            : "<tr><td colspan='2'>Ingen ratings endnu</td></tr>"}
      </tbody>
    </table>`;
        $("#ratingsModal").modal("show");
    }


    // --- sorting buttons ---
    const sortButtons = [
        { id: "sort-dfv", key: "dfvScore", type: "dfv" },
        { id: "sort-vivino", key: "vivinoScore", type: "vivino" },
        { id: "sort-date", key: "meetingDate", type: "date" },
    ];

    sortButtons.forEach(btn => {
        const el = document.getElementById(btn.id);
        if (!el) return;
        el.addEventListener("click", () => {
            sortState[btn.type] = !sortState[btn.type];
            const asc = sortState[btn.type];
            const sorted = [...ratings].sort((a, b) => {
                if (btn.key === "meetingDate") {
                    return asc
                        ? new Date(a.meetingDate) - new Date(b.meetingDate)
                        : new Date(b.meetingDate) - new Date(a.meetingDate);
                }
                return asc ? a[btn.key] - b[btn.key] : b[btn.key] - a[btn.key];
            });
            renderRatings(sorted);
        });
    });
}
