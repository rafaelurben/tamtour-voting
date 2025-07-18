FROM node:22 AS build
WORKDIR /app
COPY . .
RUN npm install
RUN npm run build --prod

FROM nginx:1.29 AS run
COPY --from=build /app/dist/voting-frontend /usr/share/nginx/html
