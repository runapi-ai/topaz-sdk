package ai.runapi.core.files;

/** Confirmation that a File was deleted. */
public final class DeletedFile {
  private String id;
  private String object;
  private boolean deleted;

  public String getId() { return id; }
  public String getObject() { return object; }
  public boolean isDeleted() { return deleted; }
}
