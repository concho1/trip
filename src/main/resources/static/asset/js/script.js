// tedu-N14 [BNLxe9YRuZ]
(function() {
  $(".tedu-N14").each(function() {
    const $block = $(this);
    $(function() {
      var contSwiper = new Swiper(".tedu-N14 .content-swiper", {
        slidesPerView: 1,
        spaceBetween: 20,
        loop: false,
        speed: 500,
        navigation: {
          nextEl: ".content-swiper .swiper-button-next",
          prevEl: ".content-swiper .swiper-button-prev",
        },
        breakpoints: {
          768: {
            slidesPerView: 2,
            spaceBetween: 20,
          },
          1024: {
            slidesPerView: 3,
            spaceBetween: 40,
          },
        },
      });
    });
  });
})();
// tedu-N45 [WmLXi9pyUS]
(function() {
  $(function() {
    $(".tedu-N45").each(function() {
      const $block = $(this);
      const $header = $(".header-container");
      const $headerHeight = $header.height();
      // HeaderHeight
      $(document).ready(function() {
        $block.css({
          "top": $headerHeight + 130 + "px"
        })
      })
      // Scroll
      $(window).on("load scroll", function() {
        const $thisTop = $(this).scrollTop();
        if ($thisTop > 0) {
          $block.addClass("sticky");
          var fromBottom = $(document).height() - $thisTop - $(window).height();
          var maxDistance = 200;
          if (fromBottom > maxDistance) {
            $block.css('bottom', '520px');
          }
          if (window.innerWidth <= 980) {
            $block.removeClass("sticky");
            $block.addClass("default");
          }
        } else {
          $block.removeClass("sticky");
          $block.css('bottom', 'auto');
        }
      });
      // Mobile Area
      $(window).on('resize', function() {
        if (window.innerWidth >= 980) {
          $block.removeClass("default");
        } else if (window.innerWidth <= 980) {
          $block.removeClass("sticky");
          $block.addClass("default");
        }
      });
    });
  });
})();
// tedu-N15 [ynlxiC75Mu]
(function() {
  $(".tedu-N15").each(function() {
    const $block = $(this);
    $(function() {
      var contSwiper = new Swiper(".tedu-N15 .content-swiper", {
        slidesPerView: 1,
        spaceBetween: 20,
        loop: false,
        speed: 500,
        navigation: {
          nextEl: ".content-swiper .swiper-button-next",
          prevEl: ".content-swiper .swiper-button-prev",
        },
        breakpoints: {
          768: {
            slidesPerView: 2,
            spaceBetween: 20,
          },
          1024: {
            slidesPerView: 3,
            spaceBetween: 40,
          },
        },
      });
    });
  });
})();