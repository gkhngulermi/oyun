function saveScore(score) {
    const playerName = document.getElementById("playerName").value || "Guest";

    fetch("/api/scores", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ player: playerName, score: score })
    }).then(res => {
        console.log("Skor kaydedildi:", score);
        loadLeaderboard();
    });
}
