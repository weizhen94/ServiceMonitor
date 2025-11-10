FROM node:20 AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build -- --configuration=production

FROM nginx:1.25-alpine
COPY --from=build /app/dist/service-monitor-frontend /usr/share/nginx/html
EXPOSE 80
