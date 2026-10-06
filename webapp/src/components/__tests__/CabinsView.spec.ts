import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount, flushPromises } from '@vue/test-utils'
import CabinsView from '../CabinsView.vue'
import axios from 'axios'

vi.mock('axios')

describe('CabinsView', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it("n'affiche pas de tableau s'il n'y a pas de cabines attribuées", async () => {
    vi.mocked(axios.get).mockResolvedValue({ data: { cabins: [] } })

    const wrapper = mount(CabinsView)
    await wrapper.find('[data-cy="fetch-cabin-attribution"]').trigger('click')
    await flushPromises()

    expect(wrapper.find('table').exists()).toBe(false)
    expect(wrapper.findAll('[data-cy="cabin-attribution-details"]')).toHaveLength(0)
  })

  it("affiche les cabines attribuées dans un tableau dans l'ordre reçu", async () => {
    const cabins = [
      { cabinId: 'K1', category: 'DELUXE', bookingId: 'booking-001' },
      { cabinId: 'K2', category: 'DELUXE', bookingId: 'booking-002' },
    ]
    vi.mocked(axios.get).mockResolvedValue({ data: { cabins } })

    const wrapper = mount(CabinsView)
    await wrapper.find('[data-cy="fetch-cabin-attribution"]').trigger('click')
    await flushPromises()

    const rows = wrapper.findAll('[data-cy="cabin-attribution-details"]')
    expect(rows).toHaveLength(2)
    expect(rows[0].find('.cabin-id-item').text()).toBe('K1')
    expect(rows[0].find('.cabin-category-item').text()).toBe('DELUXE')
    expect(rows[0].find('.cabin-bookingId-item').text()).toBe('booking-001')
    expect(rows[1].find('.cabin-id-item').text()).toBe('K2')
    expect(rows[1].find('.cabin-bookingId-item').text()).toBe('booking-002')
  })

  it('envoie le critère "bookingDateTime" par défaut', async () => {
    vi.mocked(axios.get).mockResolvedValue({ data: { cabins: [] } })

    const wrapper = mount(CabinsView)
    await wrapper.find('[data-cy="fetch-cabin-attribution"]').trigger('click')
    await flushPromises()

    expect(axios.get).toHaveBeenCalledWith('/cruises/JUPITER_MOONS_EXPLORATION_2085/cabins', {
      params: { criteria: 'bookingDateTime' },
    })
  })

  it('envoie le critère "travelers" quand il est sélectionné', async () => {
    vi.mocked(axios.get).mockResolvedValue({ data: { cabins: [] } })

    const wrapper = mount(CabinsView)
    await wrapper.find('select#criteria').setValue('travelers')
    await wrapper.find('[data-cy="fetch-cabin-attribution"]').trigger('click')
    await flushPromises()

    expect(axios.get).toHaveBeenCalledWith('/cruises/JUPITER_MOONS_EXPLORATION_2085/cabins', {
      params: { criteria: 'travelers' },
    })
  })
})