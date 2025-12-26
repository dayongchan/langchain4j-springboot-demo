import chatService from '../services/chatService';
import userService from '../services/userService';

// Mock browser APIs for Jest environment
global.TextEncoder = require('util').TextEncoder;
global.TextDecoder = require('util').TextDecoder;

// Mock userService
beforeEach(() => {
  jest.clearAllMocks();
  userService.getToken = jest.fn(() => 'mock-token');
});

describe('ChatService', () => {
  describe('constructor', () => {
    it('should initialize with correct baseUrl', () => {
      expect(chatService.baseUrl).toBe('http://localhost:8089/api/chat');
    });
  });

  describe('sendStreamingMessage', () => {
    let mockFetch;
    let mockResponse;
    let mockReadableStream;

    beforeEach(() => {
      // Mock ReadableStream
      mockReadableStream = {
        getReader: jest.fn(() => ({
          read: jest.fn().mockResolvedValueOnce({
            done: false,
            value: Buffer.from('Hello, ')
          }).mockResolvedValueOnce({
            done: false,
            value: Buffer.from('world!')
          }).mockResolvedValueOnce({
            done: true,
            value: undefined
          }),
          releaseLock: jest.fn()
        }))
      };

      mockResponse = {
        ok: true,
        status: 200,
        headers: new Headers(),
        body: mockReadableStream
      };

      mockFetch = jest.spyOn(global, 'fetch').mockResolvedValue(mockResponse);
    });

    afterEach(() => {
      mockFetch.mockRestore();
    });

    it('should make correct fetch request with conversationId', async () => {
      const onChunkReceived = jest.fn();
      const onComplete = jest.fn();
      const onError = jest.fn();

      await chatService.sendStreamingMessage(
        'test message',
        '123',
        onChunkReceived,
        onComplete,
        onError
      );

      expect(mockFetch).toHaveBeenCalledWith(
        'http://localhost:8089/api/chat/streaming?conversationId=123',
        expect.objectContaining({
          method: 'POST',
          headers: expect.objectContaining({
            'Content-Type': 'text/plain',
            'Authorization': 'Bearer mock-token'
          }),
          body: 'test message'
        })
      );
    });

    it('should handle streaming chunks correctly', async () => {
      const onChunkReceived = jest.fn();
      const onComplete = jest.fn();
      const onError = jest.fn();

      await chatService.sendStreamingMessage(
        'test message',
        '123',
        onChunkReceived,
        onComplete,
        onError
      );

      expect(onChunkReceived).toHaveBeenCalledTimes(2);
      expect(onChunkReceived).toHaveBeenNthCalledWith(1, 'Hello, ');
      expect(onChunkReceived).toHaveBeenNthCalledWith(2, 'world!');
      expect(onComplete).toHaveBeenCalledTimes(1);
      expect(onError).not.toHaveBeenCalled();
    });

    it('should handle fetch errors', async () => {
      const mockError = new Error('Network error');
      mockFetch.mockRejectedValue(mockError);

      const onChunkReceived = jest.fn();
      const onComplete = jest.fn();
      const onError = jest.fn();

      await chatService.sendStreamingMessage(
        'test message',
        '123',
        onChunkReceived,
        onComplete,
        onError
      );

      expect(onError).toHaveBeenCalledWith(expect.any(Error));
      expect(onChunkReceived).not.toHaveBeenCalled();
      expect(onComplete).not.toHaveBeenCalled();
    });
  });

  describe('sendStreamingSearchMessage', () => {
    let mockFetch;
    let mockResponse;
    let mockReadableStream;

    beforeEach(() => {
      // Mock ReadableStream
      mockReadableStream = {
        getReader: jest.fn(() => ({
          read: jest.fn().mockResolvedValueOnce({
            done: false,
            value: Buffer.from('Search result: ')
          }).mockResolvedValueOnce({
            done: false,
            value: Buffer.from('AI assistant')
          }).mockResolvedValueOnce({
            done: true,
            value: undefined
          }),
          releaseLock: jest.fn()
        }))
      };

      mockResponse = {
        ok: true,
        status: 200,
        headers: new Headers(),
        body: mockReadableStream
      };

      mockFetch = jest.spyOn(global, 'fetch').mockResolvedValue(mockResponse);
    });

    afterEach(() => {
      mockFetch.mockRestore();
    });

    it('should make correct fetch request to streaming-search endpoint', async () => {
      const onChunkReceived = jest.fn();
      const onComplete = jest.fn();
      const onError = jest.fn();

      await chatService.sendStreamingSearchMessage(
        'test message',
        '123',
        onChunkReceived,
        onComplete,
        onError
      );

      expect(mockFetch).toHaveBeenCalledWith(
        'http://localhost:8089/api/chat/streaming-search?conversationId=123',
        expect.objectContaining({
          method: 'POST',
          headers: expect.objectContaining({
            'Content-Type': 'text/plain',
            'Authorization': 'Bearer mock-token'
          }),
          body: 'test message'
        })
      );
    });

    it('should handle streaming search chunks correctly', async () => {
      const onChunkReceived = jest.fn();
      const onComplete = jest.fn();
      const onError = jest.fn();

      await chatService.sendStreamingSearchMessage(
        'test message',
        '123',
        onChunkReceived,
        onComplete,
        onError
      );

      expect(onChunkReceived).toHaveBeenCalledTimes(2);
      expect(onChunkReceived).toHaveBeenNthCalledWith(1, 'Search result: ');
      expect(onChunkReceived).toHaveBeenNthCalledWith(2, 'AI assistant');
      expect(onComplete).toHaveBeenCalledTimes(1);
      expect(onError).not.toHaveBeenCalled();
    });
  });
});
