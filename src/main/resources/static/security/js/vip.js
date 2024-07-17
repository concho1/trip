document.addEventListener("DOMContentLoaded", function() {
    const totalSpentElement = document.getElementById("totalSpent");
    const totalSpent = parseFloat(totalSpentElement.textContent.replace(/[^\d.-]/g, '')); // 총 소비 금액
    /*const vipLevel = /!*[[${vipLevel}]]*!/ 'Bronze'; // Thymeleaf에서 VIP 등급 가져오기*/
    console.log("VIP Level:", vipLevel); // 콘솔에 VIP 레벨 로그 출력
    const vipBarElement = document.getElementById("vip-bar");

    let vipWidth = "";
    let vipColor = "";

    if (totalSpent === 0) {
        vipWidth = "0%";
        vipColor = "#cf7d4e";
    } else if (totalSpent < 1000000) {
        const progress = (totalSpent / 1000000) * 19; // 100만원 미만일 때 금액에 따라 0%에서 19%까지 채움
        vipWidth = `${progress}%`;
        vipColor = "#cf7d4e";
    } else if (totalSpent < 3000000) {
        const progress = 22 + ((totalSpent - 1000000) / 2000000) * 27; // 100만원 이상 300만원 미만일 때 22%에서 49%까지 채움
        vipWidth = `${progress}%`;
        vipColor = "#bec2c9";
    } else if (totalSpent < 5000000) {
        const progress = 52 + ((totalSpent - 3000000) / 2000000) * 47; // 300만원 이상 500만원 미만일 때 52%에서 99%까지 채움
        vipWidth = `${progress}%`;
        vipColor = "#ecc43a";
    } else {
        vipWidth = "100%"; // 500만원 이상일 때 100%
        vipColor = "#0a577c";
    }

    vipBarElement.style.width = vipWidth;
    vipBarElement.style.backgroundColor = vipColor;

    // 별 표시
    const vipStars = document.querySelectorAll('.vip-star-marker');
    vipStars.forEach(function(star) {
        star.classList.remove('active'); // 모든 별표의 활성 클래스 제거
    });

    // 현재 등급에 맞는 별표에 활성 클래스를 추가하여 색상을 변경합니다
    const levels = ['bronze', 'silver', 'gold', 'platinum'];
    const activeIndex = levels.indexOf(vipLevel.toLowerCase());

    vipStars.forEach(function(star) {
        const starLevel = star.classList[1]; // 'vip-star-marker bronze' 중 'bronze'를 가져옴
        if (levels.indexOf(starLevel) <= activeIndex) {
            star.classList.add('active');
        }
    });
});



