db.createUser({
    user: "appuser",
    pwd: "apppassword",
    roles: [
        {
            role: "readWrite",
            db: "commentsdb"
        }
    ]
});
