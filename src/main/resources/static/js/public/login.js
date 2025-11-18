document.getElementById("loginForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    const API_BASE_URL = location.hostname === "localhost"
        ? "http://localhost:8080"
        : "http://157.180.23.204:8080";

    try {
        const res = await fetch(`${API_BASE_URL}/auth/login`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username, password }),
            credentials: "include"
        });

        const data = await res.json();
        if (!res.ok) throw new Error(data.error || "Login mislykkedes");

        localStorage.setItem("role", data.role);
        localStorage.setItem("username", data.username);

        if (data.role === "ADMIN") {
            window.location.href = "../internal/dashboard.html";
        } else {
            window.location.href = "../internal/meeting.html";
        }
    } catch (err) {
        alert("Forkert brugernavn eller adgangskode");
    }
});
