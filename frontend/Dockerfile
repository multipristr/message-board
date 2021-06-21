FROM node:16.3.0-alpine3.13 as builder
ENV NODE_ENV=production

ADD package.json /app/
ADD package-lock.json /app/
WORKDIR /app
RUN npm install --production
ADD . /app
RUN ["npm", "run", "build"]

FROM nginx:1.21.0-alpine
EXPOSE 80
COPY --from=builder /app/public /usr/share/nginx/html