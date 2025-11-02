
document.addEventListener("DOMContentLoaded", async () => {
    const placeholder = document.getElementById("navbar-placeholder");
    if (!placeholder) return;

    try {
        const res = await fetch("./components/navbar.html");
        if (!res.ok) throw new Error("Navbar not found");
        const html = await res.text();
        placeholder.innerHTML = html;
        placeholder.classList.add("loaded");

        // Activate current page highlight
        const currentPath = window.location.pathname;
        placeholder.querySelectorAll(".nav-link").forEach(link => {
            if (link.getAttribute("href") === currentPath) {
                link.classList.add("active");
            }
        });
    } catch (err) {
        console.error("Error loading navbar:", err);
    }
});

