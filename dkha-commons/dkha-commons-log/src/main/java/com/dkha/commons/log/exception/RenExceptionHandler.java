

package com.dkha.commons.log.exception;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.dkha.commons.log.SysLogError;
import com.dkha.commons.log.enums.LogTypeEnum;
import com.dkha.commons.log.producer.LogProducer;
import com.dkha.commons.tools.config.ModuleConfig;
import com.dkha.commons.tools.exception.ErrorCode;
import com.dkha.commons.tools.exception.ExceptionUtils;
import com.dkha.commons.tools.exception.RenException;
import com.dkha.commons.tools.utils.HttpContextUtils;
import com.dkha.commons.tools.utils.IpUtils;
import com.dkha.commons.tools.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;


/**
 * 异常处理器
 * @since 1.0.0
 */
@RestControllerAdvice
public class RenExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(RenExceptionHandler.class);
	@Autowired
	private ModuleConfig moduleConfig;
	@Autowired
	private LogProducer logProducer;

	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler(RenException.class)
	public Result handleRRException(RenException ex){
		Result result = new Result();
		result.error(ex.getCode(), ex.getMsg());

		return result;
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public Result handleDuplicateKeyException(DuplicateKeyException ex){
		Result result = new Result();
		result.error(ErrorCode.DB_RECORD_EXISTS);

		return result;
	}


	@ExceptionHandler(AccessDeniedException.class)
	public Result handleAccessDeniedException(Exception ex){
		Result result = new Result();
		result.error(ErrorCode.FORBIDDEN);

		return result;
	}

	@ExceptionHandler(Exception.class)
	public Result handleException(Exception ex){
		logger.error(ex.getMessage(), ex);

		saveLog(ex);

		return new Result().error();
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public Result handleDataIntegrityViolationException(DataIntegrityViolationException ex){
		logger.error(ex.getMessage(), ex);
		saveLog(ex);

		return new Result().error(ex.getCause().getMessage());
	}

	/**
	 * 保存异常日志
	 */
	private void saveLog(Exception ex){
		SysLogError log = new SysLogError();
		log.setType(LogTypeEnum.ERROR.value());
		log.setModule(moduleConfig.getName());

		//请求相关信息
		HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
		log.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
		log.setRequestUri(request.getRequestURI());
		log.setRequestMethod(request.getMethod());
		log.setIp(IpUtils.getIpAddr(request));
		Map<String, String> params = HttpContextUtils.getParameterMap(request);
		if(MapUtil.isNotEmpty(params)){
			log.setRequestParams(JSON.toJSONString(params));
		}

		//异常信息
		log.setErrorInfo(ExceptionUtils.getErrorStackTrace(ex));

		//保存到Redis队列里
		log.setCreateDate(new Date());
		logProducer.saveLog(log);
	}
}
