import { render, screen } from '@testing-library/react'
import { describe, expect, it } from 'vitest'
import { HostCardItem } from './HostCardItem'
import { API_URL } from '../../../services/config'

describe('HostCardItem', () => {
  it('muestra el título, país y precio por noche del alojamiento', () => {
    render(<HostCardItem
      title="Casa de prueba"
      location={{ country: 'Argentina' }}
      value={120}
      images={['casa.jpg']}
    />)

    expect(screen.getByText('Casa de prueba')).toBeVisible()
    expect(screen.getByText('Argentina')).toBeVisible()
    expect(screen.getByText('$121 por noche')).toBeVisible()
  })

  it('usa la primera imagen y actualiza los datos al cambiar las props', () => {
    const { rerender } = render(<HostCardItem
      title="Casa de prueba"
      location={{ country: 'Argentina' }}
      value={120}
      images={['primera.jpg', 'segunda.jpg']}
    />)

    expect(screen.getByRole('img', { name: 'image 2' }))
      .toHaveAttribute('src', `${API_URL}/img/primera.jpg`)

    rerender(<HostCardItem
      title="Otro alojamiento"
      location={{ country: 'Uruguay' }}
      value={80}
      images={['otra.jpg']}
    />)

    expect(screen.queryByText('Casa de prueba')).not.toBeInTheDocument()
    expect(screen.getByText('Otro alojamiento')).toBeVisible()
    expect(screen.getByText('Uruguay')).toBeVisible()
    expect(screen.getByText('$80 por noche')).toBeVisible()
    expect(screen.getByRole('img', { name: 'image 2' }))
      .toHaveAttribute('src', `${API_URL}/img/otra.jpg`)
  })
})
