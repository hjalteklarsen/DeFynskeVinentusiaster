document.getElementById("sortSelect").addEventListener("change", function () {
    const value = this.value;
    const cards = Array.from(document.querySelectorAll(".wine-card"));
    const container = document.getElementById("wine-list");

    cards.sort((a, b) => {
        const ratingA = parseFloat(a.dataset.rating);
        const ratingB = parseFloat(b.dataset.rating);
        const dateA = new Date(a.dataset.date);
        const dateB = new Date(b.dataset.date);

        switch (value) {
            case "rating-asc": return ratingA - ratingB;
            case "rating-desc": return ratingB - ratingA;
            case "date-asc": return dateA - dateB;
            case "date-desc": return dateB - dateA;
        }
    });

    cards.forEach(card => container.appendChild(card)); // re-render sorted
});
