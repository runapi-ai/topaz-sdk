package ai.runapi.core.files;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;

/** Cursor-paginated File collection. */
public final class FileListResponse {
  private String object;
  private List<FileObject> data;
  @JsonProperty("first_id") private String firstId;
  @JsonProperty("last_id") private String lastId;
  @JsonProperty("has_more") private boolean hasMore;

  public String getObject() { return object; }
  public List<FileObject> getData() { return data == null ? Collections.<FileObject>emptyList() : Collections.unmodifiableList(data); }
  public String getFirstId() { return firstId; }
  public String getLastId() { return lastId; }
  public boolean hasMore() { return hasMore; }
}
