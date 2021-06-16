FROM node:16.3.0-alpine3.13 as builder

WORKDIR /app

COPY package.json .
RUN npm install
COPY . .
RUN ["npm", "run", "build"]

FROM nginx:1.21.0-alpine
EXPOSE 80
COPY --from=builder /app/public /usr/share/nginx/html