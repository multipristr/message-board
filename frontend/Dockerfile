FROM node:16.20.2-alpine3.18 as builder
ENV NODE_ENV=production

ADD package.json package-lock.json /app/
WORKDIR /app
RUN npm install --production
ADD . /app
RUN ["npm", "run", "build"]

FROM nginx:1.27.3-alpine3.20
EXPOSE 80
COPY --from=builder /app/public /usr/share/nginx/html