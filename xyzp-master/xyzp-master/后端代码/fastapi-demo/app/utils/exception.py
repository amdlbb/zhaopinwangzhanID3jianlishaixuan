from fastapi import Request
from fastapi.exceptions import HTTPException, RequestValidationError
from fastapi.responses import JSONResponse
from .result import Result

async def http_exception_handler(request: Request, exc: HTTPException):
    return JSONResponse(
        status_code=exc.status_code,
        content=Result.error(msg=exc.detail, code=exc.status_code)
    )

async def validation_exception_handler(request: Request, exc: RequestValidationError):
    errors = []
    for err in exc.errors():
        field = " -> ".join(str(loc) for loc in err["loc"])
        errors.append(f"{field}: {err['msg']}")
    detail = "; ".join(errors)
    return JSONResponse(
        status_code=422,
        content=Result.error(msg=f"Request validation error: {detail}", code=422)
    )

async def general_exception_handler(request: Request, exc: Exception):
    # 生产环境：记日志，不暴露细节
    return JSONResponse(
        status_code=500,
        content=Result.error(msg="Internal server error", code=500)
    )
