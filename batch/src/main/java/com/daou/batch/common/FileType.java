package com.daou.batch.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileType {

	CSV("csv", ","),
	PIPETEXT("text", "|"),
	SLASHTEXT("txt", "/");

	private final String fileFormat;
	private final String seperator;

	public String getAllFilesWithFormatPattern() {
		return "*." + fileFormat;
	}

	public String getAllFilesInPathWithFormatPattern(String path) {
		return path + "/" + getAllFilesWithFormatPattern();
	}
}
