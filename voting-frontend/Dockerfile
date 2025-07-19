FROM node:22 AS build
WORKDIR /app
COPY . .
RUN npm install
RUN npm run build --omit=dev

FROM nginx:1.29 AS run
COPY --from=build /app/nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist/voting-frontend/browser /usr/share/nginx/html
