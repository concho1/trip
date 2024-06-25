const date = new Date();
let currYear = date.getFullYear();
let currMonth = date.getMonth();
const months = ['Jenuary', 'February', 'March',
    'April', 'May', 'June',
    'July', 'August', 'September',
    'October', 'November', 'December'];

const renderCalendar = () => {
    document.querySelector("#currentDate").innerHTML = `${months[currMonth]} ${currYear}`;
    let lastDay = new Date(currYear, currMonth+1, 0).getDate();
    let first_yoil = new Date(currYear, currMonth, 1).getDay();
    let last_yoil = new Date(currYear, currMonth, lastDay).getDay();
    let lastDayofLastMonth = new Date(currYear, currMonth, 0).getDate();
    let dayTag = "";

    for(let i=first_yoil; i>0; i--) {
        dayTag += `<li class="inactive">${lastDayofLastMonth - i + 1}</li>`;
    }

    for(let i=1; i<= lastDay; i++) {

        let isToday = i === date.getDate() &&
        currMonth === new Date().getMonth() &&
        currYear === new Date().getFullYear()
            ? 'active' : '';
        dayTag += `<li id="${isToday}">${i}</li>`
    }

    for(let i=last_yoil; i<6; i++) {
        dayTag += `<li class="inactive">${i - last_yoil + 1}</li>`;
    }

    document.querySelector("#days").innerHTML = dayTag;

    document.querySelectorAll("#days li").forEach(day => {
        day.addEventListener("click", (e) => {
            document.querySelectorAll("#days li").forEach(d => d.removeAttribute("id"));
            e.target.id = "active";
        });
    });

};

document.querySelector("#shang").addEventListener('click', () => {
    if(currMonth == 0) {
        currMonth = 11;
        currYear = currYear - 1;
    }else {
        currMonth = currMonth - 1;
    }
    renderCalendar();
});

document.querySelector("#xia").addEventListener('click', () => {
    if(currMonth == 11) {
        currMonth = 0;
        currYear = currYear + 1;
    }else {
        currMonth = currMonth + 1;
    }
    renderCalendar();
});

renderCalendar();