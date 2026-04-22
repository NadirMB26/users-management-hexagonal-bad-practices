package com.jcaa.usersmanagement.infrastructure.adapter.persistence.config;

public final class DatabaseConfig {
  private final String host;
  private final int port;
  private final String name;
  private final String username;
  private final String password;

  public DatabaseConfig(String host, int port, String name, String username, String password) {
    this.host = host;
    this.port = port;
    this.name = name;
    this.username = username;
    this.password = password;
  }

  public String host() { return host; }
  public int port() { return port; }
  public String name() { return name; }   
  public String username() { return username; }
  public String password() { return password; }
}
