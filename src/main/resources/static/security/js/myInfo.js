/*
document.addEventListener("DOMContentLoaded", function() {
    const links = document.querySelectorAll('.cont-title');

    // 페이지 로드 시 로컬 스토리지에서 선택된 링크를 불러오기
    const activeLinkId = localStorage.getItem('selectLinkId');
    if (activeLinkId) {
        const activeLink = document.getElementById(activeLinkId);
        if (activeLink) {
            links.forEach(l => l.classList.remove('select')); // 기존 선택 해제
            activeLink.classList.add('select'); // 선택된 링크 활성화
        }
    } else {
        // 로컬 스토리지에 저장된 값이 없을 경우 첫 번째 링크를 선택
        if (links.length > 0) {
            links[0].classList.add('select');
        }
    }

    links.forEach(link => {
        link.addEventListener('click', function() {
            // 모든 링크의 선택된 상태를 해제
            links.forEach(l => l.classList.remove('select'));

            // 클릭된 링크를 선택된 상태로 설정
            this.classList.add('select');

            // 선택된 링크의 ID를 로컬 스토리지에 저장
            localStorage.setItem('selectLinkId', this.id);
        });
    });
});
*/
