package io.github.folgue02.sapphire.exchange;

/// Representations of different MIME types.
/// @see #mime
public enum ContentType {
	// Text
	HTML("text/html; charset=UTF-8"),
	PLAIN("text/plain; charset=UTF-8"),
	CSS("text/css"),
	JAVASCRIPT("application/javascript"), // or "text/javascript"
	JSON("application/json"),
	JSON_LD("application/ld+json"),
	XML("application/xml"),
	CSV("text/csv"),
	MARKDOWN("text/markdown"),

	// Multipart / Form
	FORM_URLENCODED("application/x-www-form-urlencoded"),
	MULTIPART_FORM_DATA("multipart/form-data"),

	// Images
	PNG("image/png"),
	JPEG("image/jpeg"),
	GIF("image/gif"),
	SVG("image/svg+xml"),
	AVIF("image/avif"),
	WEBP("image/webp"),
	BMP("image/bmp"),
	ICO("image/vnd.microsoft.icon"),

	// Audio
	MP3("audio/mpeg"),
	OGG("audio/ogg"),
	AAC("audio/aac"),
	MIDI("audio/midi"),

	// Video
	MP4("video/mp4"),
	MPEG("video/mpeg"),
	AVI("video/x-msvideo"),
	OGV("video/ogg"),

	// Fonts
	WOFF("font/woff"),
	WOFF2("font/woff2"),
	OTF("font/otf"),
	TTF("font/ttf"),

	// Application / Binary
	OCTET_STREAM("application/octet-stream"),
	PDF("application/pdf"),
	ZIP("application/zip"),
	GZIP("application/gzip"),
	EPUB("application/epub+zip"),
	JAVAR("application/java-archive"),
	MSWORD("application/msword"),
	XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
	PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation"),
	DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),

	// Misc
	CALENDAR("text/calendar");

	public final String mime;

	ContentType(String mime) {
		this.mime = mime;
	}
}
