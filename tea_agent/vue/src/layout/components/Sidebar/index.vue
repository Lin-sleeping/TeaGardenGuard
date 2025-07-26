<template>
    <div :class="{'has-logo':showLogo}" :style="{ backgroundColor: settings.sideTheme === 'theme-dark' ? variables.menuBg : variables.menuLightBg }">
        <logo v-if="showLogo" :collapse="isCollapse" />
        <el-scrollbar :class="settings.sideTheme" wrap-class="scrollbar-wrapper">
            <el-menu
                :default-active="activeMenu"
                :collapse="isCollapse"
                :background-color="settings.sideTheme === 'theme-dark' ? variables.menuBg : variables.menuLightBg"
                :text-color="settings.sideTheme === 'theme-dark' ? variables.menuText : 'rgba(0,0,0,.65)'"
                :unique-opened="true"
                :active-text-color="settings.theme"
                :collapse-transition="false"
                mode="vertical"
            >
                <sidebar-item
                    v-for="(route, index) in filteredSidebarRouters"
                    :key="route.path  + index"
                    :item="route"
                    :base-path="route.path"
                />
            </el-menu>
        </el-scrollbar>
    </div>
</template>

<script>
import { mapGetters, mapState } from "vuex";
import Logo from "./Logo";
import SidebarItem from "./SidebarItem";
import variables from "@/assets/styles/variables.scss";

export default {
    components: { SidebarItem, Logo },
    computed: {
        ...mapState(["settings"]),
        ...mapGetters(["sidebarRouters", "sidebar"]),
        filteredSidebarRouters() {
            // 只保留物联网下的设备分类和设备列表，系统管理下只保留用户管理和日志管理
            return this.sidebarRouters.map(menu => {
                if (menu.name === '物联网' || menu.meta?.title === '物联网') {
                    return {
                        ...menu,
                        children: (menu.children || []).filter(child =>
                            child.name === '设备分类' ||
                            child.meta?.title === '设备分类' ||
                            child.name === '设备列表' ||
                            child.meta?.title === '设备列表'
                        )
                    }
                }
                if (menu.name === '系统管理' || menu.meta?.title === '系统管理') {
                    return {
                        ...menu,
                        children: (menu.children || []).filter(child =>
                            child.name === '用户管理' ||
                            child.meta?.title === '用户管理' ||
                            child.name === '日志管理' ||
                            child.meta?.title === '日志管理'
                        )
                    }
                }
                return menu;
            });
        },
        activeMenu() {
            const route = this.$route;
            const { meta, path } = route;
            // if set path, the sidebar will highlight the path you set
            if (meta.activeMenu) {
                return meta.activeMenu;
            }
            return path;
        },
        showLogo() {
            return this.$store.state.settings.sidebarLogo;
        },
        variables() {
            return variables;
        },
        isCollapse() {
            return !this.sidebar.opened;
        }
    }
};
</script>
