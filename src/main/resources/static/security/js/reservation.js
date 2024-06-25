function showTabFromHash() {
    const hash = location.hash || '#all';
    const tabs = document.querySelectorAll('.myedu-cont-wrap');
    tabs.forEach(tab => {
        tab.style.display = tab.id === hash.substring(1) ? 'block' : 'none';
    });

    const tabMenus = document.querySelectorAll('.tab-menu');
    tabMenus.forEach(menu => {
        menu.classList.toggle('active', `#${menu.querySelector('a').getAttribute('href').substring(1)}` === hash);
    });
}

window.addEventListener('hashchange', showTabFromHash);
window.addEventListener('load', showTabFromHash);
