const API_BASE = "http://localhost:8080";

export const getVideos = async () => {
  const res = await fetch(`${API_BASE}/movie-info/list`);
  if (!res.ok) throw new Error("Failed to fetch videos");
  return res.json();
};

export const getStreamUrl = (id) =>
  `${API_BASE}/stream/with-id/${id}`;
