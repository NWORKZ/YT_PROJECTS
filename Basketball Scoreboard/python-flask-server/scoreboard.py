from flask import Flask, request, redirect, url_for, render_template;

app = Flask(__name__)
cScore = 0

@app.route("/")
def index():
    global cScore
    return render_template("scoreboard.html", score = cScore)

@app.route("/reset")
def reset():
    global cScore
    cScore = 0
    return redirect("http://192.168.0.123:5000");

#for wemos
@app.route("/scored")
def scored():
    willAdd = request.args.get("willAdd")
    
    if(willAdd == "scored"):
        global cScore
        cScore = cScore + 2
        
        return render_template("scoreboard.html", score = cScore)

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)