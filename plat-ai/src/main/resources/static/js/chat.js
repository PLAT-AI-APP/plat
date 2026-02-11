
class ChatSocket {

    constructor(url, model) {
        this._url = url;
        this._model = model;
        this.isLoading = false
        this._decoder = new TextDecoder('utf-8');

    }

    error(errorHandler) {
        this._errorHandler = errorHandler;
        return this
    }

    finally(finallyHandler) {
        this._finallyHandler = finallyHandler
        return this;
    }

    async stream(message, callback) {
        try {
            await this._runStream(message, callback);
        } catch (error) {
            this._errorHandler?.(error);
        } finally {
            this._finallyHandler?.()
        }
    }

    async _runStream(message, callback) {
        this.isLoading = true
        // Plain text 스트림 요청
        const res = await fetch(this._url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                sessionId: 'session-test',
                message: message,
                persona: 1,
                model: this._model,
            })
        });

        // HTTP 상태 코드 체크
        if (!res.ok) {
            const errorText = await res.text();
            throw new Error(`서버 오류 (${res.status}): 잠시 후 다시 시도해주세요.`);
        }

        const reader = res.body.getReader();
        let fullText = '';

        while (true) {
            const { done, value } = await reader.read();

            if (done) {
                this.isLoading = false
                break;
            }

            fullText += this._decoder.decode(value, { stream: true });
            callback(fullText);
        }
    }
}