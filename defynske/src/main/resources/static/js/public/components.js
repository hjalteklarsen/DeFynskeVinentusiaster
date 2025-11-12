document.addEventListener("DOMContentLoaded", async () => {
    // Where to insert the components
    const topbarPlaceholder = document.createElement("div");
    const navbarPlaceholder = document.createElement("div");

    // Insert at top of body
    document.body.insertBefore(navbarPlaceholder, document.body.firstChild);
    document.body.insertBefore(topbarPlaceholder, document.body.firstChild);

    // Helper to load each HTML fragment
    async function loadComponent(target, path) {
        try {
            const res = await fetch(path);
            if (!res.ok) throw new Error(`Failed to load ${path}`);
            const html = await res.text();
            target.outerHTML = html;
        } catch (err) {
            console.error(err);
        }
    }

    // Load topbar first
    await loadComponent(topbarPlaceholder, "components/topbar.html");
    // Load navbar second
    await loadComponent(navbarPlaceholder, "components/navbar.html");

    requestAnimationFrame(() => {
        // Get just the filename from the current path
        const currentPage = window.location.pathname.split("/").pop() || "index.html";

        // Loop over navbar links
        document.querySelectorAll(".nav-link").forEach(link => {
            const linkPage = link.getAttribute("href").split("/").pop();
            link.classList.toggle("active", linkPage === currentPage);
        });
    });
});

