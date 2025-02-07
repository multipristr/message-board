FROM node:16.20.2-alpine3.18 as builder
ENV NODE_ENV=production

ADD package.json package-lock.json /app/
WORKDIR /app
RUN npm install --omit=dev
ADD . /app
RUN ["npm", "run", "build"]

FROM nginx:1.27.4-alpine3.21
EXPOSE 80
COPY --from=builder /app/public /usr/share/nginx/html