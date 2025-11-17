document.addEventListener('DOMContentLoaded', () => {
    loadDashboardStats();
});

async function loadDashboardStats() {
    try {
        const [members, meetings, ratings] = await Promise.all([
            fetch('/api/members').then(res => res.json()),
            fetch('/api/meetings').then(res => res.json()),
            fetch('/api/ratings').then(res => res.json())
        ]);

        document.getElementById('memberCount').textContent = members.length;
        document.getElementById('meetingCount').textContent = meetings.length;
        document.getElementById('ratingCount').textContent = ratings.length;
    } catch (err) {
        console.error('Fejl ved hentning af dashboard-data:', err);
    }
}
