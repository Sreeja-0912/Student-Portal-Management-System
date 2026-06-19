# FROM node:22-alpine AS build
# WORKDIR /app
# COPY package*.json ./
# RUN npm install --legacy-peer-deps
# COPY . .
# RUN npm run build
#
# FROM nginx:1.27-alpine
# COPY nginx.conf /etc/nginx/conf.d/default.conf
# COPY --from=build /app/dist/student-portal-frontend/browser /usr/share/nginx/html
# EXPOSE 80
# CMD ["nginx", "-g", "daemon off;"]


# 🟢 Step 1: Use a clean, lightweight Nginx web server image
FROM nginx:1.27-alpine

# 🟢 Step 2: Copy your single-page routing file from your machine
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 🟢 Step 3: Copy your locally built files straight from your computer
# This bypasses the container network completely!
COPY dist/student-portal-frontend/browser /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]

