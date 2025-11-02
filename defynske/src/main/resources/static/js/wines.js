async function loadWines() {
    const res = await fetch('/wines');
    if (!res.ok) {
        console.error("Failed to fetch wines");
        return;
    }

    const wines = await res.json();
    const tbody = document.getElementById('wineTableBody');

    tbody.innerHTML = wines.map(w => `
    <tr>
      <td>${w.name}</td>
      <td>${w.region || ''}</td>
      <td>${w.country || ''}</td>
      <td>${w.year || ''}</td>
      <td>${w.grapeVariety || ''}</td>
      <td>${w.source || ''}</td>
    </tr>
  `).join('');
}

document.getElementById('wineForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const wine = {
        name: document.getElementById('name').value,
        region: document.getElementById('region').value,
        country: document.getElementById('country').value,
        year: parseInt(document.getElementById('year').value) || null,
        grapeVariety: document.getElementById('grapeVariety').value,
        source: document.getElementById('source').value,
        externalId: document.getElementById('externalId').value
    };

    const res = await fetch('/wines', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(wine)
    });

    if (res.ok) {
        await loadWines();
        document.getElementById('wineForm').reset();
        bootstrap.Modal.getInstance(document.getElementById('addWineModal')).hide();
    } else {
        alert("Failed to add wine.");
    }
});

loadWines();
