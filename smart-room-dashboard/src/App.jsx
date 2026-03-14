import { useEffect, useMemo, useState } from "react";
import "./App.css";

const DEFAULT_BASE_URL = "http://localhost:8080";
const DEFAULT_ROOM_ID = "1";

function MetricCard({ title, value, subtitle }) {
  return (
    <div className="card metric-card">
      <p className="card-title">{title}</p>
      <p className="card-value">{value}</p>
      <p className="card-subtitle">{subtitle}</p>
    </div>
  );
}

function StatusItem({ label, active }) {
  return (
    <div className="status-item">
      <span>{label}</span>
      <span className={active ? "badge badge-on" : "badge badge-off"}>
        {active ? "ON" : "OFF"}
      </span>
    </div>
  );
}

function formatDateTime(ts) {
  if (!ts) return "-";
  const d = new Date(ts);
  if (Number.isNaN(d.getTime())) return ts;
  return d.toLocaleString();
}

function formatTime(ts) {
  if (!ts) return "-";
  const d = new Date(ts);
  if (Number.isNaN(d.getTime())) return ts;
  return d.toLocaleTimeString();
}

export default function App() {
  const [baseUrl, setBaseUrl] = useState(DEFAULT_BASE_URL);
  const [roomId, setRoomId] = useState(DEFAULT_ROOM_ID);
  const [room, setRoom] = useState(null);
  const [latest, setLatest] = useState(null);
  const [sensors, setSensors] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [lastRefresh, setLastRefresh] = useState(null);
  const [autoRefresh, setAutoRefresh] = useState(true);

  const fetchAll = async () => {
    setLoading(true);
    setError("");

    try {
      const [roomRes, latestRes, sensorsRes] = await Promise.all([
        fetch(`${baseUrl}/api/rooms/${roomId}`),
        fetch(`${baseUrl}/api/sensors/latest`),
        fetch(`${baseUrl}/api/sensors`),
      ]);

      if (!roomRes.ok) {
        throw new Error(`Room request failed: ${roomRes.status}`);
      }
      if (!latestRes.ok) {
        throw new Error(`Latest sensor request failed: ${latestRes.status}`);
      }
      if (!sensorsRes.ok) {
        throw new Error(`Sensors request failed: ${sensorsRes.status}`);
      }

      const [roomData, latestData, sensorsData] = await Promise.all([
        roomRes.json(),
        latestRes.json(),
        sensorsRes.json(),
      ]);

      const filteredSensors = Array.isArray(sensorsData)
        ? sensorsData.filter(
            (s) => String(s.room?.id ?? s.roomId ?? roomId) === String(roomId)
          )
        : [];

      setRoom(roomData);
      setLatest(latestData);
      setSensors(filteredSensors.slice(-12));
      setLastRefresh(new Date());
    } catch (e) {
      setError(e.message || "Impossible de récupérer les données du backend.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAll();
  }, []);

  useEffect(() => {
    if (!autoRefresh) return;

    const intervalId = setInterval(fetchAll, 5000);
    return () => clearInterval(intervalId);
  }, [autoRefresh, baseUrl, roomId]);

  const recentRows = useMemo(() => {
    return [...sensors].reverse();
  }, [sensors]);

  return (
    <div className="app">
      <div className="container">
        <header className="hero card">
          <div>
            <h1>Smart Room Dashboard</h1>
            <p>
              Test en temps réel de l’architecture Arduino → Python → Spring
              Boot → H2
            </p>
          </div>

          <div className="controls">
            <input
              type="text"
              value={baseUrl}
              onChange={(e) => setBaseUrl(e.target.value)}
              placeholder="Backend URL"
            />

            <input
              type="text"
              value={roomId}
              onChange={(e) => setRoomId(e.target.value)}
              placeholder="Room ID"
            />

            <button onClick={fetchAll}>Rafraîchir</button>

            <button
              className={autoRefresh ? "secondary active" : "secondary"}
              onClick={() => setAutoRefresh((v) => !v)}
            >
              {autoRefresh ? "Auto ON" : "Auto OFF"}
            </button>
          </div>
        </header>

        {error && (
          <div className="card error-box">
            <h3>Connexion au backend impossible</h3>
            <p>{error}</p>
            <p>Vérifie que Spring Boot est lancé et que l’URL est correcte.</p>
          </div>
        )}

        <section className="metrics-grid">
          <MetricCard
            title="Température"
            value={latest?.temperature != null ? `${latest.temperature} °C` : "-"}
            subtitle={
              latest?.timestamp
                ? `Mesure : ${formatDateTime(latest.timestamp)}`
                : "Aucune mesure"
            }
          />

          <MetricCard
            title="Humidité"
            value={latest?.humidity != null ? `${latest.humidity} %` : "-"}
            subtitle={room?.name ? `Salle : ${room.name}` : "Salle inconnue"}
          />

          <MetricCard
            title="Occupation"
            value={room?.occupancyCount != null ? room.occupancyCount : "-"}
            subtitle={room?.occupied ? "Salle occupée" : "Salle vide"}
          />

          <MetricCard
            title="Dernière synchro"
            value={lastRefresh ? lastRefresh.toLocaleTimeString() : "-"}
            subtitle={loading ? "Chargement..." : "Backend actif"}
          />
        </section>

        <section className="content-grid">
          <div className="card">
            <h2>Historique récent</h2>

            {sensors.length === 0 ? (
              <p className="empty-text">Aucune donnée disponible.</p>
            ) : (
              <div className="history-list">
                {sensors.map((item, index) => (
                  <div key={item.id ?? index} className="history-item">
                    <div>
                      <strong>{formatTime(item.timestamp)}</strong>
                    </div>
                    <div>Température : {item.temperature ?? "-"} °C</div>
                    <div>Humidité : {item.humidity ?? "-"} %</div>
                  </div>
                ))}
              </div>
            )}
          </div>

          <div className="card">
            <h2>État courant de la salle</h2>

            <div className="status-list">
              <StatusItem label="Chauffage" active={!!room?.heatingOn} />
              <StatusItem label="Ventilation" active={!!room?.ventilationOn} />
              <StatusItem label="Lumière" active={!!room?.lightOn} />
              <StatusItem label="Porte ouverte" active={!!room?.doorOpen} />
            </div>
          </div>
        </section>

        <section className="card">
          <h2>Dernières mesures enregistrées</h2>

          <div className="table-wrapper">
            <table>
              <thead>
                <tr>
                  <th>Heure</th>
                  <th>Device</th>
                  <th>Température</th>
                  <th>Humidité</th>
                  <th>Salle</th>
                </tr>
              </thead>
              <tbody>
                {recentRows.length === 0 ? (
                  <tr>
                    <td colSpan="5" className="empty-cell">
                      Aucune donnée trouvée.
                    </td>
                  </tr>
                ) : (
                  recentRows.map((item, index) => (
                    <tr key={item.id ?? index}>
                      <td>{formatDateTime(item.timestamp)}</td>
                      <td>{item.deviceId || "-"}</td>
                      <td>{item.temperature ?? "-"} °C</td>
                      <td>{item.humidity ?? "-"} %</td>
                      <td>{item.room?.name || `Room ${roomId}`}</td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </section>
      </div>
    </div>
  );
}