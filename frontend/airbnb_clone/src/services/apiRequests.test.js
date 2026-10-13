import { http, HttpResponse } from 'msw'
import { describe, expect, it, vi } from 'vitest'
import { getAllProperties } from './apiRequests'
import { API_URL } from './config'
import { server } from '../test/msw/server'

describe('getAllProperties', () => {
  it('devuelve la lista de alojamientos del catálogo público', async () => {
    const properties = await getAllProperties()

    expect(properties).toEqual([
      {
        id: 'property-test-1',
        title: 'Casa de prueba',
        username: 'owner@example.test',
        category: 'HOUSE',
        subCategory: 'BEACH',
        description: 'Alojamiento ficticio para tests',
        value: 120,
        active: true,
        imageUrls: ['property-test-1.jpg'],
        location: { id: 'location-test-1', country: 'Argentina', city: 'Mar del Plata' },
        bathroom: 1,
        bed: 2,
        bedroom: 1,
      },
    ])
  })

  it('devuelve una lista vacía cuando no hay alojamientos', async () => {
    server.use(http.get(`${API_URL}/property`, () => HttpResponse.json([])))

    const properties = await getAllProperties()

    expect(properties).toEqual([])
  })

  it('devuelve undefined y registra el error cuando la API responde 500', async () => {
    const logError = vi.spyOn(console, 'error').mockImplementation(() => {})
    server.use(http.get(`${API_URL}/property`, () => HttpResponse.json({
      type: 'about:blank',
      title: 'Internal Server Error',
      status: 500,
      detail: 'Ocurrió un error interno. Intentá nuevamente más tarde.',
      instance: '/v1/api/property',
    }, { status: 500, headers: { 'Content-Type': 'application/problem+json' } })))

    const properties = await getAllProperties()

    expect(properties).toBeUndefined()
    expect(logError).toHaveBeenCalledExactlyOnceWith('error de respuesta')
  })

  it('devuelve undefined y registra el error cuando falla la red', async () => {
    const logError = vi.spyOn(console, 'error').mockImplementation(() => {})
    server.use(http.get(`${API_URL}/property`, () => HttpResponse.error()))

    const properties = await getAllProperties()

    expect(properties).toBeUndefined()
    expect(logError).toHaveBeenCalledExactlyOnceWith('Error de red:', expect.any(Error))
  })
})
