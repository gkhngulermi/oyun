// Veri modeli
@kotlinx.serialization.Serializable
data class ScoreRequest(val player: String, val score: Int)

fun Application.module() {
    // DB bağlantısı
    Database.connect("jdbc:h2:file:./build/db", driver = "org.h2.Driver")

    transaction {
        create(Scores)
    }

    routing {
        // Ana endpoint
        get("/") {
            call.respondText("Hello Game World!")
        }

        // Statik dosyalar
        staticResources("/", "static")

        // Skor kaydet
        post("/api/scores") {
            val scoreReq = call.receive<ScoreRequest>()

            transaction {
                Scores.insert {
                    it[player] = scoreReq.player
                    it[score] = scoreReq.score
                }
            }

            call.respondText("Skor kaydedildi: ${scoreReq.player} -> ${scoreReq.score}")
        }

        // Skor tablosu
        get("/api/scores") {
            val topScores = transaction {
                Scores.selectAll()
                    .orderBy(Scores.score, SortOrder.DESC)
                    .limit(10)
                    .map { "${it[Scores.player]}: ${it[Scores.score]}" }
            }
            call.respond(topScores.joinToString("\n"))
        }
    }
}
