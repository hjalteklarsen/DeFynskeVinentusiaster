document.addEventListener('DOMContentLoaded', () => {
    loadMembers();
    loadMeetings();
    document.getElementById('meetingForm').addEventListener('submit', async e => {
        e.preventDefault();
        await createMeeting();
    });
});

async function loadMembers() {
    try {
        const res = await fetch('/internal/members');
        if (!res.ok) throw new Error('Kunne ikke hente medlemmer');
        const members = await res.json();

        const select = document.getElementById('hostUsername');
        select.innerHTML = '<option value="">Vælg vært...</option>';

        members.forEach(member => {
            const option = document.createElement('option');
            option.value = member.username;
            option.textContent = `${member.displayName}`;
            select.appendChild(option);
        });
    } catch (err) {
        console.error('Fejl ved hentning af medlemmer:', err);
    }
}
async function loadMeetings() {
    const container = document.getElementById('meetings-container');
    container.innerHTML = '';

    try {
        const meetings = await fetch('/internal/meeting').then(r => r.json());
        if (!meetings.length) {
            container.innerHTML = '<div class="col text-center text-muted"><p>Ingen møder oprettet endnu 🍷</p></div>';
            return;
        }

        meetings.forEach(meeting => {
            const col = document.createElement('div');
            col.className = 'col-md-4 mb-4';

            col.innerHTML = `
        <div class="meeting-card" onclick="openMeeting(${meeting.id})">
          <div class="img" style="background-image:url('../images/bg_4.jpg')"></div>
          <div class="text">
            <h4>${meeting.description}</h4>
            <p class="mb-1"><i class="fa fa-calendar mr-2"></i>${meeting.date}</p>
            <p class="mb-1"><i class="fa fa-user mr-2"></i>${meeting.hostUsername}</p>
            ${meeting.location ? `<p class="mb-0"><i class="fa fa-map-marker mr-2"></i>${meeting.location}</p>` : ''}
          </div>
        </div>`;
            container.appendChild(col);
        });
    } catch (err) {
        console.error('Fejl ved hentning af møder:', err);
    }
}

async function createMeeting() {
    const meeting = {
        description: document.getElementById('description').value,
        date: document.getElementById('date').value,
        hostUsername: document.getElementById('hostUsername').value
    };
    console.log("Sending meeting:", meeting);
    try {
        const res = await fetch('/internal/meeting', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(meeting)
        });

        if (res.ok) {
            $('#createMeetingModal').modal('hide');
            await loadMeetings();
        } else {
            console.error('Server returnerede fejl:', await res.text());
        }
    } catch (err) {
        console.error('Fejl ved oprettelse af møde:', err);
    }
}


function openMeeting(id) {
    window.location.href = `meetingDetails.html?id=${id}`;
}
