# Build stage
FROM node:20.19.0 AS builder
WORKDIR /app
COPY . .
RUN npm install
RUN npm run build --configuration=production

# Run stage
FROM nginx:alpine
COPY --from=builder /app/dist/task-manager-ui/browser /usr/share/nginx/html
COPY ./nginx.conf /etc/nginx/conf.d/default.conf
