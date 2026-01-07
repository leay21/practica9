# Práctica 9: Aplicaciones Móviles y Dispositivos Vestibles

Este proyecto implementa una solución completa de IoT integrando un **Dispositivo Vestible (Wear OS)** con una **Aplicación Móvil (Android)**, utilizando sensores físicos y servicios en la nube para sincronización en tiempo real.

## 🚀 Funcionalidades

### ⌚ App Wear OS (Módulo `:app`)
* **Contador de Pasos/Movimiento:** Utiliza el sensor **Acelerómetro** del hardware para detectar sacudidas y movimientos de muñeca.
* **Optimización de Batería:** El uso de sensores se detiene automáticamente cuando la pantalla se apaga o la app pasa a segundo plano (`onPause`).
* **Persistencia de Datos:** El conteo se guarda localmente mediante **SharedPreferences**, permitiendo reiniciar el reloj sin perder el progreso.
* **Notificaciones Push:** Servicio en segundo plano (`FirebaseMessagingService`) para recibir alertas remotas incluso con la app cerrada.

### 📱 App Móvil (Módulo `:mobile`)
* **Monitorización en Tiempo Real:** Interfaz desarrollada en **Jetpack Compose** que muestra instantáneamente la actividad del reloj.
* **Sincronización Automática:** No requiere actualización manual (pull-to-refresh) gracias a los listeners de Firebase.

### ☁️ Backend & Integración (Firebase)
* **Realtime Database:** Sincronización de baja latencia entre el reloj y el celular.
* **Cloud Messaging (FCM):** Sistema de notificaciones para enviar avisos al reloj desde la consola.

## 🛠️ Stack Tecnológico
* **Lenguaje:** Kotlin
* **UI:** XML (Wear OS) & Jetpack Compose (Mobile)
* **Cloud:** Google Firebase (Realtime DB, FCM)
* **Hardware APIs:** SensorManager (Accelerometer)

## 📋 Requisitos de Ejecución
1.  **Wear OS:** API 30 o superior (Probado en API 36).
2.  **Android Mobile:** API 24 o superior.
3.  **Conectividad:** Ambos dispositivos deben tener acceso a internet para la sincronización con Firebase.

## 📸 Capturas

| Pantalla del reloj mostrando los movimientos detectados | Pantalla del celular mostrando los mismos datos a través de firebase | Notificación de prueba enviada al reloj |
|:---:|:---:|:---:|
| ![Reloj](https://github.com/user-attachments/assets/8e59df47-961d-4847-9259-4f87c4970e7d)  | ![Celular](https://github.com/user-attachments/assets/8105d5c8-39f3-4552-aa5e-757cc2e21160) | ![Notificaciones](https://github.com/user-attachments/assets/52987c43-82c7-4fd3-822e-543eb9acfd07) |

## 👤 Autor
**Toral Alvarez Yael Adair**
