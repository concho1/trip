/*document.addEventListener("DOMContentLoaded", function() {
    const totalSpentElement = document.getElementById("totalSpent");
    const totalSpent = parseFloat(totalSpentElement.textContent.replace(/[^\d.-]/g, '')); // 총 소비 금액
    const vipLevelElement = document.getElementById("vip-level");
    const vipBarElement = document.getElementById("vip-bar");
    const vipStarsElement = document.getElementById("vip-stars");

    let vipLevel = "";
    let vipWidth = "";
    let vipColor = "";

    if (totalSpent === 0) {
        vipLevel = "Bronze";
        vipWidth = "0%";
        vipColor = "#cf7d4e";
    } else if (totalSpent < 1000000) {
        vipLevel = "Bronze";
        vipWidth = `${(totalSpent / 10000)}%`; // 100만원 미만일 때 금액에 따라 채워지는 정도 조절
        vipColor = "#cf7d4e";
    } else if (totalSpent < 3000000) {
        vipLevel = "Silver";
        vipWidth = `${((totalSpent - 1000000) / 20000)}%`; // 100만원 이상 300만원 미만일 때
        vipColor = "#bec2c9";
    } else if (totalSpent < 5000000) {
        vipLevel = "Gold";
        vipWidth = `${((totalSpent - 3000000) / 20000)}%`; // 300만원 이상 500만원 미만일 때
        vipColor = "#ecc43a";
    } else {
        vipLevel = "Platinum";
        vipWidth = `${((totalSpent - 5000000) / 20000)}%`; // 500만원 이상일 때
        vipColor = "#3cabce";
    }

    vipLevelElement.textContent = vipLevel;
    vipBarElement.style.width = vipWidth;
    vipBarElement.style.backgroundColor = vipColor;

    // 별 표시
    vipStarsElement.innerHTML = ""; // 기존 별표 초기화
    for (let i = 1; i <= 10; i++) {
        const star = document.createElement("span");
        star.classList.add("vip-star");
        star.innerHTML = "&#9733;";  // 유니코드 별 문자

        if (i <= Math.floor(totalSpent / 1000000)) {
            star.classList.add("filled");
        }

        vipStarsElement.appendChild(star);
    }
});*/
document.addEventListener("DOMContentLoaded", function() {
    const totalSpentElement = document.getElementById("totalSpent");
    const totalSpent = parseFloat(totalSpentElement.textContent.replace(/[^\d.-]/g, '')); // 총 소비 금액
    const vipLevelElement = document.getElementById("vip-level");
    const vipBarElement = document.getElementById("vip-bar");
    const vipStarsElement = document.getElementById("vip-stars");

    let vipLevel = "";
    let vipWidth = "";
    let vipColor = "";

    if (totalSpent === 0) {
        vipLevel = "Bronze";
        vipWidth = "0%";
        vipColor = "#cf7d4e";
    } else if (totalSpent < 1000000) {
        vipLevel = "Bronze";
        const progress = (totalSpent / 1000000) * 29; // 100만원 미만일 때 금액에 따라 0%에서 29%까지 채움
        vipWidth = `${progress}%`;
        vipColor = "#cf7d4e";
    } else if (totalSpent < 3000000) {
        vipLevel = "Silver";
        const progress = 30 + ((totalSpent - 1000000) / 2000000) * 19; // 100만원 이상 300만원 미만일 때 30%에서 49%까지 채움
        vipWidth = `${progress}%`;
        vipColor = "#bec2c9";
    } else if (totalSpent < 5000000) {
        vipLevel = "Gold";
        const progress = 50 + ((totalSpent - 3000000) / 2000000) * 49; // 300만원 이상 500만원 미만일 때 50%에서 99%까지 채움
        vipWidth = `${progress}%`;
        vipColor = "#ecc43a";
    } else {
        vipLevel = "Platinum";
        vipWidth = "100%"; // 500만원 이상일 때 100%
        vipColor = "#3cabce";
    }

    vipLevelElement.textContent = vipLevel;
    vipBarElement.style.width = vipWidth;
    vipBarElement.style.backgroundColor = vipColor;

    // 별 표시
    vipStarsElement.innerHTML = ""; // 기존 별표 초기화
    for (let i = 1; i <= 10; i++) {
        const star = document.createElement("span");
        star.classList.add("vip-star");
        star.innerHTML = "&#9733;";  // 유니코드 별 문자

        if (i <= Math.floor(totalSpent / 1000000)) {
            star.classList.add("filled");
        }

        vipStarsElement.appendChild(star);
    }
});


