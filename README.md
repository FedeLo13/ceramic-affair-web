# Ceramic Affair Web

**Ceramic Affair Web** es la versión desplegada de una aplicación web desarrollada como parte del Trabajo de Fin de Grado **“Creación de Ceramic Affair Web: Una Aplicación Web para la Venta Online de Piezas de Cerámica”**.  
Su objetivo es ofrecer una plataforma funcional, accesible y visualmente coherente con la marca *Ceramic Affair*, permitiendo a una artesana independiente gestionar su catálogo y facilitar la compra de sus piezas de cerámica.

## 🚀 Funcionalidades principales

- **Catálogo online de productos** con imágenes, descripciones y precios.
- **Sistema de autenticación** para que la artesana pueda gestionar su inventario.
- **Panel de administración** para crear, editar y eliminar productos.
- **Gestión de eventos y mercadillos** para promover la actividad de la ceramista.
- **Sistema de contacto** y **registro en newsletter** para clientes.
- **Notificaciones informativas** a los usuarios.
- **Arquitectura basada en API REST** para la comunicación entre frontend y backend.

---

## 🏗️ Tecnologías utilizadas

### Backend
- **Java 17**
- **Spring Boot**
- Spring Security, Spring Data JPA, JWT
- Base de datos relacional (MySQL)

### Frontend
- **React** + **Vite**
- **TypeScript**
- React Router

---

## 🐳 Despliegue con Docker

Esta versión incluye un sistema de despliegue basado en **Docker Compose**, permitiendo levantar el frontend, backend y base de datos con un solo comando.

### Comandos principales

```bash
docker compose build
docker compose up -d
```

Los contenedores definidos incluyen:
- backend – API REST en Spring Boot
- frontend – aplicación web React
- db – base de datos del proyecto
- traefik - gestión de certificados SSL

## 📜 Licencia y autoría

Proyecto desarrollado por Federico López como parte del Trabajo de Fin de Grado del Grado en Ingeniería Informática de la Universidad de Cádiz.
Para consultas: fedevlopez17@gmail.com
