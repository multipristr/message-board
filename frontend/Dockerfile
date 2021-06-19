#syntax=docker/dockerfile:experimental
FROM node:16.3.0-alpine3.13 as builder

ADD package.json /app/
WORKDIR /app
RUN --mount=target=/app/node_modules,type=cache npm install
ADD . /app
RUN ["npm", "run", "build"]

FROM nginx:1.21.0-alpine
EXPOSE 80
COPY --from=builder /app/public /usr/share/nginx/html