FROM node:24.12.0-alpine3.23 as builder
ENV NODE_ENV=production

ADD package.json package-lock.json /app/
WORKDIR /app
RUN npm install --omit=dev
ADD . /app
RUN ["npm", "run", "build"]

FROM nginx:1.29.2-alpine3.22
EXPOSE 80
COPY --from=builder /app/public /usr/share/nginx/html