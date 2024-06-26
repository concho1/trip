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
// tedu-N18 [JsLXmnn4Nl]
(function() {
  $(function() {
    $(".tedu-N18").each(function() {
      const $block = $(this);
      const $header = $(".header-container");
      // Background Color
      if ($header.hasClass("darkmode")) {
        $block.addClass("darkmode");
        $block.removeClass("lightmode");
      } else if ($header.hasClass("lightmode")) {
        $block.addClass("lightmode");
        $block.removeClass("darkmode");
      }
    });
  });
})();
// tedu-N22 [gClXmnN54I]
(function() {
  $(function() {
    $(".tedu-N22").each(function() {
      const $block = $(this);
      // Tab Menu 
      $block.find(".tab-menu").on("click", function() {
        const $this = $(this);
        $this.addClass("active");
        $this.siblings().removeClass("active");
      })
    });
  });
})();
// tedu-N23 [tolxmNN5c7]
(function() {
  $(function() {
    $(".tedu-N23").each(function() {
      const $block = $(this);
      const $header = $(".header-container");
      // Background Color
      if ($header.hasClass("darkmode")) {
        $block.addClass("darkmode");
        $block.removeClass("lightmode");
      } else if ($header.hasClass("lightmode")) {
        $block.addClass("lightmode");
        $block.removeClass("darkmode");
      }
    });
  });
})();
// tedu-N27 [JKlXMNn5oI]
(function() {
  $(".tedu-N27").each(function() {
    const $block = $(this);
    $(function() {
      var contSwiper = new Swiper(".tedu-N27 .review-swiper", {
        slidesPerView: 1,
        spaceBetween: 12,
        loop: false,
        centeredSlides: false,
        speed: 500,
        navigation: {
          nextEl: ".review-swiper .swiper-button-next",
          prevEl: ".review-swiper .swiper-button-prev",
        },
        breakpoints: {
          768: {
            slidesPerView: 1.5,
            spaceBetween: 12,
          },
          980: {
            slidesPerView: 1,
            spaceBetween: 12,
          },
          996: {
            slidesPerView: 1,
            spaceBetween: 12,
          },
          1024: {
            slidesPerView: 1,
            spaceBetween: 20,
          },
        },
      });
    });
  });
})();
// tedu-N29 [UnLxmNn5uh]
(function() {
  $(function() {
    $(".tedu-N29").each(function() {
      const $block = $(this);
      // accordion
      $block.find(".acc-btn").on("click", function() {
        const $this = $(this);
        $this.parents(".acc-item").addClass("active");
        $this.parents(".acc-item").siblings().removeClass("active");
      })
    });
  });
})();
// tedu-N31 [BGlxMNn5ZF]
(function() {
  $(function() {
    $(".tedu-N31").each(function() {
      const $block = $(this);
      //Bookmark
      $block.find(".ico-heart").on("click", function() {
        const $this = $(this);
        $this.parents(".card-wrap").toggleClass("badge");
      });
    });
  });
})();