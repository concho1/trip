// tedu-N1 [phlXe9YR3t]
(function() {
  $(function() {
    $(".tedu-N1").each(function() {
      const $block = $(this);
      // Lang
      $block.find(".header-lang").on("click", function() {
        const $this = $(this);
        $this.toggleClass("lang-active");
      });
      // Condition
      const hdArea = $block.find(".header-area");
      const hdSearch = $block.find(".header-search");
      // Menu
      $block.find(".menu-open").on("click", function() {
        const $this = $(this);
        if (hdSearch.length > 0) {
          hdSearch.removeClass("is-active");
        }
        if (window.innerWidth <= 980) {
          $block.find(".header-container").addClass("mo-active");
          $this.hide();
          $block.find(".menu-close").show();
        } else {
          $block.find(".header-area").addClass("is-active");
          $this.hide();
          $block.find(".menu-close").show();
        }
      });
      $block.find(".menu-close").on("click", function() {
        const $this = $(this);
        if (window.innerWidth <= 980) {
          $block.find(".header-container").removeClass("mo-active");
          $this.hide();
          $block.find(".menu-open").show();
        } else {
          $block.find(".header-area").removeClass("is-active");
          $this.hide();
          $block.find(".menu-open").show();
        }
      });
      // Search
      $block.find(".search-btn").on("click", function() {
        $(this).hide();
        $block.find(".search-close").show();
        if (hdArea.length > 0) {
          hdArea.removeClass("is-active");
          $block.find(".menu-open").show();
          $block.find(".menu-close").hide();
        }
        $block.find(".header-search").addClass("is-active");
      });
      $block.find(".search-close").on("click", function() {
        $(this).hide();
        $block.find(".search-btn").show();
        $block.find(".header-search").removeClass("is-active");
      });
      // Mobile Menu
      $block.find(".mhd-menu-box").each(function() {
        const $this = $(this);
        const $thislink = $this.find(".mhd-menu-title");
        $thislink.on("click", function() {
          if (!$(this).parent().hasClass("is-active")) {
            $(".mhd-menu-box").removeClass("is-active");
          }
          $(this).parents(".mhd-menu-box").toggleClass("is-active");
        });
      });
      // Mobile Area
      $(window).on("resize", function() {
        if (window.innerWidth >= 980) {
          $block.find(".header-container").removeClass("mo-active");
          $block.find(".menu-close").hide();
          $block.find(".menu-open").show();
        } else if (window.innerWidth <= 980) {
          $block.find(".header-area").removeClass("is-active");
          $block.find(".menu-close").hide();
          $block.find(".menu-open").show();
          $block.find(".header-lang").removeClass("lang-active");
          $block.find(".header-search").removeClass("is-active");
        }
      });
    });
  });
})();
// tedu-N7 [LwLxe9yR8z]
(function() {
  $(".tedu-N7").each(function() {
    const $block = $(this);
    $(function() {
      var visualSwiper = new Swiper(".tedu-N7 .visual-swiper", {
        speed: 600,
        parallax: true,
        loop: true,
        touchEventsTarget: "wrapper",
        slidesPerView: "auto",
        autoplay: {
          delay: 2500,
          disableOnInteraction: false,
        },
        pagination: {
          el: ".tedu-N7 .visual-swiper .control-wrap .swiper-pagination",
          clickable: "true",
        },
        on: {
          slideChange: function() {
            var activeSlide = this.slides[this.activeIndex];
            if ($(activeSlide).hasClass('btype')) {
              $(this.$el).addClass('btype-swiper');
            } else {
              $(this.$el).removeClass('btype-swiper');
            }
          }
        },
      });
      // Swiper Play, Pause Button
      const pauseButton = $block.find(".swiper-button-pause");
      const playButton = $block.find(".swiper-button-play");
      playButton.hide();
      pauseButton.show();
      pauseButton.on("click", function() {
        visualSwiper.autoplay.stop();
        playButton.show();
        pauseButton.hide();
      });
      playButton.on("click", function() {
        visualSwiper.autoplay.start();
        playButton.hide();
        pauseButton.show();
      });
    });
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
})();
// tedu-N8 [WOlXe9Yrd9]
(function() {
  $(".tedu-N8").each(function() {
    const $block = $(this);
    $(function() {
      var contSwiper = new Swiper(".tedu-N8 .content-swiper", {
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
      //Bookmark
      $block.find(".ico-bookmark").on("click", function() {
        const $this = $(this);
        $this.parents(".card-wrap").toggleClass("badge");
      });
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
// tedu-N10 [jrLxE9yrhb]
(function() {
  $(function() {
    $(".tedu-N10").each(function() {
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
// tedu-N12 [VilXE9yRp8]
(function() {
  $(function() {
    $(".tedu-N12").each(function() {
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
// tedu-N17 [tlLXE9ys0M]
(function() {
  $(function() {
    $(".tedu-N17").each(function() {
      const $block = $(this);
      // Site
      $block.find(".site-wrap").on("click", function() {
        const $this = $(this);
        $this.toggleClass("site-active");
      });
      // Mobile Area
      $(window).on('resize', function() {
        if (window.innerWidth <= 980) {
          $block.find(".site-wrap").removeClass("site-active");
        }
      });
    });
  });
})();
// tedu-N3 [FXLxEE3jA5]
(function() {
  $(function() {
    $(".tedu-N3").each(function() {
      const $block = $(this);
      // Lang
      $block.find(".header-lang").on("click", function() {
        const $this = $(this);
        $this.toggleClass("lang-active");
      });
      // Condition
      const hdArea = $block.find(".header-area");
      const hdSearch = $block.find(".header-search");
      // Menu
      $block.find(".menu-open").on("click", function() {
        const $this = $(this);
        if (hdSearch.length > 0) {
          hdSearch.removeClass("is-active");
        }
        if (window.innerWidth <= 980) {
          $block.find(".header-container").addClass("mo-active");
          $this.hide();
          $block.find(".menu-close").show();
        } else {
          $block.find(".header-area").addClass("is-active");
          $this.hide();
          $block.find(".menu-close").show();
        }
      });
      $block.find(".menu-close").on("click", function() {
        const $this = $(this);
        if (window.innerWidth <= 980) {
          $block.find(".header-container").removeClass("mo-active");
          $this.hide();
          $block.find(".menu-open").show();
        } else {
          $block.find(".header-area").removeClass("is-active");
          $this.hide();
          $block.find(".menu-open").show();
        }
      });
      // Search
      $block.find(".search-btn").on("click", function() {
        $(this).hide();
        $block.find(".search-close").show();
        if (hdArea.length > 0) {
          hdArea.removeClass("is-active");
          $block.find(".menu-open").show();
          $block.find(".menu-close").hide();
        }
        $block.find(".header-search").addClass("is-active");
      });
      $block.find(".search-close").on("click", function() {
        $(this).hide();
        $block.find(".search-btn").show();
        $block.find(".header-search").removeClass("is-active");
      });
      // Mobile Menu
      $block.find(".mhd-menu-box").each(function() {
        const $this = $(this);
        const $thislink = $this.find(".mhd-menu-title");
        $thislink.on("click", function() {
          if (!$(this).parent().hasClass("is-active")) {
            $(".mhd-menu-box").removeClass("is-active");
          }
          $(this).parents(".mhd-menu-box").toggleClass("is-active");
        });
      });
      // Mobile Area
      $(window).on("resize", function() {
        if (window.innerWidth >= 980) {
          $block.find(".header-container").removeClass("mo-active");
          $block.find(".menu-close").hide();
          $block.find(".menu-open").show();
        } else if (window.innerWidth <= 980) {
          $block.find(".header-area").removeClass("is-active");
          $block.find(".menu-close").hide();
          $block.find(".menu-open").show();
          $block.find(".header-lang").removeClass("lang-active");
          $block.find(".header-search").removeClass("is-active");
        }
      });
    });
  });
})();
// tedu-N47 [RSlXeE3jI3]
(function() {
  $(function() {
    $(".tedu-N47").each(function() {
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
// tedu-N20 [hNLXLKsn6F]
(function() {
  $(function() {
    $(".tedu-N20").each(function() {
      const $block = $(this);
      const $header = $(".header-container");
      const $headerHeight = $header.height();
      // HeaderHeight
      $(document).ready(function() {
        $block.css({
          "top": $headerHeight + 50 + "px"
        })
      })
      //Bookmark
      $block.find(".ico-bookmark").on("click", function() {
        const $this = $(this);
        $this.parents(".sidebar").toggleClass("badge");
      });
      // Scroll
      $(window).on("load scroll", function() {
        const $thisTop = $(this).scrollTop();
        if ($thisTop > 0) {
          $block.addClass("sticky");
          var fromBottom = $(document).height() - $thisTop - $(window).height();
          var maxDistance = 200;
          if (fromBottom > maxDistance) {
            $block.css('bottom', '280px');
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