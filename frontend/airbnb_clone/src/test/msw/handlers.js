import { http, HttpResponse } from 'msw'
import { API_URL } from '../../services/config'

// GET /v1/api/property → PropertyResponse[] de /v3/api-docs (T-009).
// La API expone imageUrls; HostCardItem todavía recibe images (T-012).
export const handlers = [
  http.get(`${API_URL}/property`, () => HttpResponse.json([
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
  ])),
]
