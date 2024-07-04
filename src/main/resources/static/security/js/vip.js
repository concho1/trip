/*
document.addEventListener("DOMContentLoaded", function() {
    const completedBookings = 3;  // 사용자의 결제 완료 건수
    const vipLevelElement = document.getElementById("vip-level");
    const vipBarElement = document.getElementById("vip-bar");

    let vipLevel = "";
    let vipWidth = "";
    let vipColor = "";

    if (completedBookings < 2) {
        vipLevel = "VIP 브론즈";
        vipWidth = "20%";
        vipColor = "#cd7f32";
    } else if (completedBookings < 5) {
        vipLevel = "VIP 실버";
        vipWidth = "40%";
        vipColor = "#c0c0c0";
    } else if (completedBookings < 10) {
        vipLevel = "VIP 골드";
        vipWidth = "60%";
        vipColor = "#ffd700";
    } else {
        vipLevel = "VIP 플래티넘";
        vipWidth = "80%";
        vipColor = "#e5e4e2";
    }

    vipLevelElement.textContent = vipLevel;
    vipBarElement.style.width = vipWidth;
    vipBarElement.style.backgroundColor = vipColor;
});
*/
document.addEventListener("DOMContentLoaded", function() {
    const completedBookings = 3;  // 사용자의 결제 완료 건수
    const vipLevelElement = document.getElementById("vip-level");
    const vipBarElement = document.getElementById("vip-bar");
    const vipStarsElement = document.getElementById("vip-stars");

    let vipLevel = "";
    let vipWidth = "";
    let vipColor = "";

    if (completedBookings === 0 ) {
        vipLevel = "Bronze";
        vipWidth = "0";
        vipColor = "#ffd700";
    } else if (completedBookings === 1 ) {
        vipLevel = "Bronze";
        vipWidth = "10%";
        vipColor = "#ffd700";
    }else if (completedBookings === 2 ) {
        vipLevel = "Silver";
        vipWidth = "20%";
        vipColor = "#ffd700";
    } else if (completedBookings === 3 ) {
        vipLevel = "Silver";
        vipWidth = "30%";
        vipColor = "#ffd700";
    } else if (completedBookings === 4 ) {
        vipLevel = "Silver";
        vipWidth = "30%";
        vipColor = "#ffd700";
    } else if (completedBookings === 5 ) {
        vipLevel = "Gold";
        vipWidth = "50%";
        vipColor = "#ffd700";
    } else if (completedBookings === 6 ) {
        vipLevel = "Gold";
        vipWidth = "60%";
        vipColor = "#ffd700";
    } else if (completedBookings === 7 ) {
        vipLevel = "Gold";
        vipWidth = "70%";
        vipColor = "#ffd700";
    } else if (completedBookings === 8 ) {
        vipLevel = "Gold";
        vipWidth = "80%";
        vipColor = "#ffd700";
    } else if (completedBookings === 9 ) {
        vipLevel = "Gold";
        vipWidth = "90%";
        vipColor = "#ffd700";
    } else {
        vipLevel = "Platinum";
        vipWidth = "100%";
        vipColor = "#ffd700";
    }

    vipLevelElement.textContent = vipLevel;
    vipBarElement.style.width = vipWidth;
    vipBarElement.style.backgroundColor = vipColor;

    // 별 표시
    for (let i = 1; i <= 10; i++) {
        const star = document.createElement("span");
        star.classList.add("vip-star");
        star.innerHTML = "&#9733;";  // 유니코드 별 문자

        if (i <= completedBookings) {
            star.classList.add("filled");
        }

        vipStarsElement.appendChild(star);
    }
});
