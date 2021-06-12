package com.game.prs.service;

import static com.game.prs.I18N.TCP_SERVER_LOG;
import static com.game.prs.I18N.WELCOME_MESSAGE;

import com.game.prs.game.HumanPlayer;
import com.game.prs.game.Session;
import com.game.prs.model.SessionState;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import javax.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

/**
 * Service which open TCP port for accepting players over the network.
 */
@Service
@Slf4j
public class TcpGameService extends AbstractGameServiceImpl {

  @Value("${game.tcp.enabled:false}")
  private boolean enabledTcpServer;

  @Value("${game.tcp.port:6969}")
  private int port;

  @Autowired
  private ThreadPoolTaskScheduler taskScheduler;

  @Override
  @PostConstruct
  public void start() {
    if (!enabledTcpServer) {
      return;
    }
    log.info(String.format(TCP_SERVER_LOG, port));
    taskScheduler.execute(tcpServer());
  }

  private Runnable tcpServer() {
    return new Runnable() {
      @SneakyThrows
      @Override
      public void run() {
        ServerSocket serverSocket = new ServerSocket(port);
        while(true) {
          Socket socket = serverSocket.accept();
          taskScheduler.execute(handleConnection(socket));
        }
      }
    };
  }

  private Runnable handleConnection(Socket socket) {
    return new Runnable() {
      @SneakyThrows
      @Override
      public void run() {
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();

        outputStream.write(WELCOME_MESSAGE.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();

        HumanPlayer humanPlayer = new HumanPlayer(
            inputStream, outputStream
        );
        Session session = newSession(humanPlayer);
        try {
          while (session.getSessionState() != SessionState.SESSION_FINISHED) {
            session.update();
          }
        } finally {
          session.clearSubscriptions();
          IOUtils.closeQuietly(socket);
        }
      }
    };
  }
}
