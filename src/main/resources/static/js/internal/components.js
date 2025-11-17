document.addEventListener("DOMContentLoaded", async () => {
    const navbarPlaceholder = document.createElement("div");
    document.body.insertBefore(navbarPlaceholder, document.body.firstChild);

    async function loadComponent(target, path) {
        try {
            const res = await fetch(path);
            if (!res.ok) throw new Error(`Failed to load ${path}`);
            target.outerHTML = await res.text();
        } catch (err) {
            console.error(err);
        }
    }

    // load internal navbar
    await loadComponent(navbarPlaceholder, "../components/internalNavbar.html");

    // Highlight active link
    const currentPage = window.location.pathname.split("/").pop();
    const links = document.querySelectorAll(".nav-link");
    links.forEach(link => {
        const href = link.getAttribute("href");
        if (href === currentPage) link.classList.add("active");
    });
});
