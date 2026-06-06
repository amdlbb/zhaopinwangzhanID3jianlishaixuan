<template>
  <div class="map-route-overlay" @click.self="closeOverlay">
    <div class="map-route-modal">
      <!-- 头部 -->
      <div class="modal-header">
        <button class="close-btn" @click="closeOverlay">✕</button>
      </div>

      <!-- 地图容器 -->
      <div id="route-map" class="route-map">

        <!-- 路线结果 -->
        <div v-if="routeResult" class="route-result">
          <span>距离：约 {{ routeResult.distance }} 公里</span>
          <span class="route-result-sep">|</span>
          <span>预计耗时：约 {{ routeResult.duration }} 分钟</span>
        </div>
        <div v-else-if="locating" class="route-result route-result-hint">
          正在定位，请稍候...
        </div>
      </div>

        <!-- 出行方式切换 -->
        <div class="route-buttons">
          <button
              v-for="mode in routeModes"
              :key="mode.key"
              :class="['route-btn', { active: activeMode === mode.key }]"
              @click="switchMode(mode.key)"
          >
            {{ mode.icon }} {{ mode.label }}
          </button>
          <div v-if="routeResult" class="route-summary">
            约 {{ routeResult.distance }} 公里 / {{ routeResult.duration }} 分钟
          </div>
        </div>

      </div>
    </div>
</template>

<script>
import AMapLoader from "@amap/amap-jsapi-loader";

const COMPANY_POS = [112.588003, 26.89771]; // 南华大学红湘校区

export default {
  name: "MapRoute",
  data() {
    return {
      map: null,
      userPosition: null,
      userAddress: "",
      activeMode: "driving",
      routeResult: null,
      locating: false,
      routeModes: [
        { key: "driving", label: "驾车", icon: "🚗" },
        { key: "walking", label: "步行", icon: "🚶" },
        // { key: "transit", label: "公交", icon: "🚌" },
      ],
    };
  },
  mounted() {
    this.initMap();
  },
  beforeDestroy() {
    this.map?.destroy();
  },
  methods: {
    closeOverlay() {
      this.$router.push("/contact");
    },
    initMap() {
      window._AMapSecurityConfig = {
        securityJsCode: "4bac65f180d4a9695a57bb371d961948",
      };
      AMapLoader.load({
        key: "726f0d6d59794c4de783fdef2e2b0b69",
        version: "2.0",
        plugins: [
          "AMap.Geolocation",
          "AMap.Driving",
          "AMap.Geocoder",
          "AMap.Walking",
          "AMap.Transfer",
          "AMap.Scale",
          "AMap.ToolBar",
        ],
      })
          .then((AMap) => {
            this.map = new AMap.Map("route-map", {
              zoom: 14,
              center: COMPANY_POS,
              viewMode: "2D",
            });

            // 公司标记
            new AMap.Marker({
              position: COMPANY_POS,
              title: "南华大学红湘校区",
            }).addTo(this.map);

            // 用户定位
            this.locateUser(AMap);
          })
          .catch((e) => {
            console.error("地图加载失败:", e);
          });
    },
    locateUser(AMap) {
      this.locating = true;
      const geolocation = new AMap.Geolocation({
        enableHighAccuracy: true,
        timeout: 10000,
        buttonPosition: "RB",
        zoomToAccuracy: true,
      });
      this.map.addControl(geolocation);

      geolocation.getCurrentPosition((status, result) => {
        this.locating = false;
        if (status === "complete") {
          this.userPosition = [result.position.lng, result.position.lat];
          console.log('用户位置定位成功:', this.userPosition)
          // 逆地理编码 → 地址
          const geocoder = new AMap.Geocoder();
          geocoder.getAddress(this.userPosition, (s, data) => {
            if (s === "complete" && data.regeocode) {
              this.userAddress = data.regeocode.formattedAddress;
              console.log("用户地址:", this.userAddress)
            } else {
              this.userAddress = "地址解析失败";
              console.error("地址解析失败:", data);
              console.log(
                  "逆地理编码失败:",
                  data.info,
                  data.message,
                  data.result
              )
            }
          });

          /*// 红色用户标记
          new AMap.Marker({
            position: this.userPosition,
            icon: "//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-red.png",
            title: "我的位置",
          }).addTo(this.map);*/

          // 自动规划驾车路线
          this.doRoute(AMap, "driving");
        } else {
          this.userAddress = "定位失败，请检查浏览器定位权限";
          console.log("定位失败:", status, result)
        }
      });
      console.log('当前位置', this.userAddress)
    },
    switchMode(mode) {
      if (mode === this.activeMode || !this.userPosition) return;
      this.activeMode = mode;
      console.log('切换按钮被触发了，即将切换到', mode)
      this.doRoute(window.AMap, mode);
    },
    doRoute(AMap, mode) {
      console.log('开始规划路线并尝试渲染，本次规划的方式为:{}', mode)
      if (!this.map || !this.userPosition) return;

      // 清除旧路线覆盖物（保留 Marker）
      this.map.remove(
          (this.map.getAllOverlays && this.map.getAllOverlays('polyline')) || []
      );

      let routePlugin;
      if (mode === "driving") {
        routePlugin = new AMap.Driving({
          policy: AMap.DrivingPolicy.LEAST_TIME,
        });
      } else if (mode === "walking") {
        routePlugin = new AMap.Walking();
      } else {
        routePlugin = new AMap.Transfer();
      }

      routePlugin.search(this.userPosition, COMPANY_POS, (status, result) => {
        console.log('路线规划结果:', status)
        if (status === "complete" && result.routes && result.routes.length) {
          const route = result.routes[0];
          this.routeResult = {
            distance: (route.distance / 1000).toFixed(1),
            duration: Math.round(route.time / 60),
          };

          // 绘制路线折线
          if (route.steps) {
            let path = route.steps.reduce(
                (acc, step) => acc.concat(step.path),
                []
            );
            path = path.map(p => [p.lng, p.lat]);

            console.log('path length:', path.length, 'first point:', path);
            const colorMap = { driving: "#3366ff", walking: "#00cc66", transit: "#ff9900" };
            var p = new AMap.Polyline({
              path,
              map: this.map,
              strokeColor: colorMap[mode] || "#3366ff",
              strokeWeight: 6,
              strokeOpacity: 0.8,
            })
            console.log('Polyline返回', p)
                // p.setMap(this.map);
          }

          this.map.setFitView(null, false, [50, 50, 50, 50]);
        } else {
          this.routeResult = null;
          console.warn("路线规划失败:", result);
        }
      });
    },
  },
};
</script>

<style scoped>

/* 路线信息展示样式 */
.route-summary {
  font-size: 18px;
  margin-left: 500px;
  color: #333;
  white-space: nowrap; /* 防止文字换行，影响布局 */
  font-weight: 500;
}

.map-route-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.55);
  z-index: 9999;
  display: flex;
  justify-content: center;
  align-items: center;
}

.map-route-modal {
  width: 90vw;
  max-width: 900px;
  height: 85vh;
  max-height: 700px;
  background: #fff;
  border-radius: 12px;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.modal-header {
  display: flex;
  align-items: center;
  padding: 16px 24px;
  border-bottom: 1px solid #e5e7eb;
  flex-shrink: 0;
}

.modal-header h2 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
}

.close-btn {
  justify-content: center;
  margin-left: auto;
  width: 32px;
  height: 32px;
  border: none;
  background: #f3f4f6;
  border-radius: 50%;
  font-size: 16px;
  cursor: pointer;
  display: flex;
  align-items: center;
  color: #6b7280;
  transition: all 0.2s;
}

.close-btn:hover {
  background: #e5e7eb;
  color: #111827;
}

.route-info {
  padding: 12px 24px;
  border-bottom: 1px solid #e5e7eb;
  flex-shrink: 0;
}

.route-info-row {
  font-size: 14px;
  margin-bottom: 6px;
  display: flex;
  gap: 4px;
}

.route-label {
  color: #6b7280;
  font-weight: 500;
  white-space: nowrap;
}

.route-value {
  color: #1f2937;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.route-buttons {
  display: flex;
  gap: 8px;
  margin: 10px 0 8px;
}

.route-btn {
  padding: 6px 18px;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  background: #fff;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s;
}

.route-btn.active,
.route-btn:hover {
  background: #1a3a5c;
  color: #fff;
  border-color: #1a3a5c;
}

.route-result {
  font-size: 14px;
  color: #1a3a5c;
  font-weight: 500;
  padding: 6px 0;
}

.route-result-sep {
  margin: 0 12px;
  color: #9ca3af;
}

.route-result-hint {
  color: #6b7280;
  font-weight: normal;
}

.route-map {
  flex: 1;
  min-height: 0;
}

@media (max-width: 768px) {
  .map-route-modal {
    width: 100vw;
    height: 100vh;
    max-width: none;
    max-height: none;
    border-radius: 0;
  }
}
</style>
