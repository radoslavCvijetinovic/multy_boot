package com.example.multy_boot;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.util.StringUtils;

@SpringBootApplication
public class MultyBootApplication {

	private static final String SPRING_CONFIG_LOCATION = "spring.config.location";

	private static final String RUN_SANDBOX = "run.sandbox";

	public static void main(String[] args) {

		runApplication(args, "live");

		if (shouldRunSandbox(args)) {
			runApplication(args, "sandbox");
		}

	}

	private static void runApplication(String[] args, String profile) {
		String springConfigLocation = resolveSpringConfigLocation(args, profile);
		SpringApplicationBuilder appBuilder = new SpringApplicationBuilder(
				MultyBootApplication.class);
		if (!StringUtils.isEmpty(springConfigLocation)) {
			appBuilder.properties(SPRING_CONFIG_LOCATION + ":file:"
					+ springConfigLocation);
		} else {
			appBuilder.properties(SPRING_CONFIG_LOCATION + ":classpath:/"
					+ profile + "-application.properties");
		}
		appBuilder.build().run(args);
	}

	private static boolean shouldRunSandbox(String[] args) {
		String runSandboxArg = getArg(args, RUN_SANDBOX);
		if ("true".equals(runSandboxArg)) {
			return true;
		}

		String hostname = "localhost";
		try {
			hostname = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
		}

		if (hostname.contains(".ccbill.") && !hostname.contains(".ash1.")
				&& !hostname.contains(".nld1.")) {
			return true;
		}

		return false;
	}

	private static String resolveSpringConfigLocation(String[] args,
			String profile) {

		if (StringUtils.isEmpty(profile)) {
			return "";
		}

		String confLocationArg = getArg(args, SPRING_CONFIG_LOCATION);

		return confLocationArg.replace("wap-frontflex", "wap-frontflex-"
				+ profile);
	}

	private static String getArg(String[] args, String key) {
		String result = "";
		for (int i = 0; i < args.length; i++) {
			if (key.equals(args[i].split("=")[0])) {
				result = args[i].split("=")[1];
				break;
			}
		}
		return result;
	}
}
