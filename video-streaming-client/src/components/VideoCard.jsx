import { useEffect, useRef } from "react";
import { getStreamUrl } from "../api/videoService";

const VideoCard = ({ video }) => {
  const videoRef = useRef(null);

  useEffect(() => {
    generateThumbnail();
  }, []);

  const generateThumbnail = () => {
    const tempVideo = document.createElement("video");
    tempVideo.src = getStreamUrl(video.id);
    tempVideo.muted = true;
    tempVideo.preload = "metadata";
    tempVideo.crossOrigin = "anonymous";

    tempVideo.addEventListener("loadedmetadata", () => {
      tempVideo.currentTime = Math.min(2, tempVideo.duration / 2);
    });

    tempVideo.addEventListener("seeked", () => {
      const canvas = document.createElement("canvas");
      canvas.width = tempVideo.videoWidth;
      canvas.height = tempVideo.videoHeight;

      const ctx = canvas.getContext("2d");
      ctx.drawImage(tempVideo, 0, 0);

      if (videoRef.current) {
        videoRef.current.poster = canvas.toDataURL("image/png");
      }

      tempVideo.remove();
    });
  };

  return (
    <div className="card">
      <div className="card-header">{video.name}</div>

      <video
        ref={videoRef}
        controls
        preload="metadata"
        src={getStreamUrl(video.id)}
      />

      <div className="card-footer">{video.description}</div>
    </div>
  );
};

export default VideoCard;
