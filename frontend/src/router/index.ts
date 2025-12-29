import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth' // Pfad zu deinem Pinia Store
import LessonView from '../views/LessonView.vue'
import DashboardView from '../views/DashboardView.vue'
import LoginView from '../views/LoginView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      redirect: '/dashboard' // Standardmäßig zum Dashboard leiten
    },
    { path: '/register', name: 'register', component: () => import('../views/RegisterView.vue') },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    },
    {
      path: '/lesson/:slug',
      name: 'lesson-view',
      component: LessonView
    },
    {
      path: '/dashboard',
      name: 'dashboard',
      component: DashboardView,
      meta: { requiresAuth: true } // Markierung für geschützte Route
    }
  ]
})

// Navigation Guard: Prüft die Berechtigung vor jedem Wechsel
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()

  // Wenn die Route Auth erfordert und der User nicht eingeloggt ist
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    next({ name: 'login' })
  }
  // Wenn der User eingeloggt ist und zur Login-Seite will -> zum Dashboard
  else if (to.name === 'login' && authStore.isAuthenticated) {
    next({ name: 'dashboard' })
  }
  else {
    next()
  }
})

export default router
